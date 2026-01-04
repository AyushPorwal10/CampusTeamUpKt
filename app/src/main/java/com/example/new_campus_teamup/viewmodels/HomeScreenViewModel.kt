package com.example.new_campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.UiState
import com.example.new_campus_teamup.clean_code.PostType
import com.example.new_campus_teamup.clean_code_1.RepostPostConfig
import com.example.new_campus_teamup.clean_code_1.SavePostConfig
import com.example.new_campus_teamup.clean_code_1.ViewPostHandlerFactory
import com.example.new_campus_teamup.clean_code_1.ViewPostResult
import com.example.new_campus_teamup.helper.CheckNetworkConnectivity
import com.example.new_campus_teamup.myactivities.UserData
import com.example.new_campus_teamup.myactivities.UserManager
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.myrepository.HomeScreenRepository
import com.example.new_campus_teamup.room.ProjectEntity
import com.example.new_campus_teamup.room.RoleEntity
import com.example.new_campus_teamup.room.VacancyEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val homeScreenRepository: HomeScreenRepository,
    private val userManager: UserManager,
    private val firebaseAuth: FirebaseAuth,
    private val viewPostHandlerFactory: ViewPostHandlerFactory,
    private val networkMonitor: CheckNetworkConnectivity
) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> get() = _userData.asStateFlow()


    private val _FCMToken = MutableStateFlow<String>("")
    val FCMToken: StateFlow<String> get() = _FCMToken.asStateFlow()

    // role
    private val _rolesUiState = MutableStateFlow<UiState<List<RoleDetails>>>(UiState.Idle)
    val rolesUiState: StateFlow<UiState<List<RoleDetails>>> get() = _rolesUiState

    private var lastVisibleRole: DocumentSnapshot? = null
    private var lastRolePostedOn: String = ""


    init {
        Log.d("HomeScreenViewModel", "HomeScreenViewModel init called")
    }


    private val _isVacancyLoading = MutableStateFlow(false)
    val isVacancyLoading: StateFlow<Boolean> = _isVacancyLoading.asStateFlow()

    // vacancy
    private val _vacancyUiState = MutableStateFlow<UiState<List<VacancyDetails>>>(UiState.Idle)
    val vacancyUiState: StateFlow<UiState<List<VacancyDetails>>> get() = _vacancyUiState.asStateFlow()

    // projects

    private val _listOfAllProjects = MutableStateFlow<List<ProjectDetails>>(emptyList())
    val listOfAllProjects: StateFlow<List<ProjectDetails>> = _listOfAllProjects


    private val _isProjectLoading = MutableStateFlow(false)
    val isProjectLoading: StateFlow<Boolean> = _isProjectLoading


    private var lastProject: DocumentSnapshot? = null
    private var lastFetchedUserId: String? = null



    private val _currentUserImage = MutableStateFlow("")
    val currentUserImage: StateFlow<String> = _currentUserImage.asStateFlow()


    private val _homeScreenUiEvent = MutableSharedFlow<String?>()
    val homeScreenUiEvent: SharedFlow<String?> = _homeScreenUiEvent.asSharedFlow()

    private val _reportPostUiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val reportPostUiState: StateFlow<UiState<Unit>> = _reportPostUiState.asStateFlow()

    // this is to fetch initial projects when screen loads

    init {
        fetchProjects()
        observeUserData()
    }


    val idsOfSavedRoles: StateFlow<List<RoleEntity>> = homeScreenRepository.fetchSavedRoleIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val listOfSavedVacancy: StateFlow<List<VacancyEntity>> = homeScreenRepository.fetchSavedVacancyIds()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val listOfSavedProjects : StateFlow<List<ProjectEntity>> = homeScreenRepository.fetchSavedProjectIds()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun startOperation(block: suspend () -> Unit) {
        viewModelScope.launch {

            if (!networkMonitor.isConnectedNow()) {
                _homeScreenUiEvent.emit("No internet connection. Please retry later.")
                return@launch
            }
            try {
                block()
            } catch (toe: TimeoutCancellationException) {
                _homeScreenUiEvent.emit( "Request timed out. Check your connection.")
            } catch (e: Exception) {
                Log.e("HomeScreenVM", "Unexpected error", e)
                _homeScreenUiEvent.emit( "Something went wrong. Please try again.")
            }
        }
    }

    private fun observeUserData() {
        startOperation {
            userData.filterNotNull().collectLatest { data ->
                val userId = data.userId
                if (userId != lastFetchedUserId) {
                    lastFetchedUserId = userId
                    Log.d(
                        "HomeScreenVM",
                        "User: ${data.userId} (${data.phoneNumber}) ${data.userName}"
                    )
                    fetchSavedData(userId)
                }
            }
        }
    }

    private fun fetchSavedData(userId: String) {
        viewModelScope.launch {
            launch {
                homeScreenRepository.fetchSavedItems(userId, "project_saved", "FetchedProject")
            }

            launch {
                homeScreenRepository.fetchSavedItems(userId, "saved_roles", "FetchedRole")
            }
            launch {

                homeScreenRepository.fetchSavedItems(userId , "saved_vacancy" , "FetchedVacancy")
            }
        }
    }


    private fun fetchProjects() {
        startOperation {
            _isProjectLoading.value = true
            val initialProject = homeScreenRepository.fetchProjects(lastProject)

            if (!initialProject.isEmpty) {
                lastProject = initialProject.last()
                _listOfAllProjects.update { current ->
                    current + initialProject.toObjects(ProjectDetails::class.java)
                }
                _isProjectLoading.value = false
            } else {
                _isProjectLoading.value = false
            }
        }
    }


    // when current user wants to save a specific project

    fun saveProject(projectDetails: ProjectDetails) {
        startOperation {
            val data = userData.filterNotNull().first()
            val result = viewPostHandlerFactory.getHandler(PostType.PROJECT)
                .savePost(config = SavePostConfig(
                    postDto = projectDetails,
                    savedBy = data.userId
                ))
            getSavePostMessage(result, PostType.PROJECT)?.let { message ->
                _homeScreenUiEvent.emit(message)
            }
        }
    }


    fun saveRole(roleDetails: RoleDetails) {
        startOperation {
            val data = userData.filterNotNull().first()
            val result = viewPostHandlerFactory.getHandler(PostType.ROLE)
                .savePost(config = SavePostConfig(
                    postDto = roleDetails,
                    savedBy = data.userId
                ))
            getSavePostMessage(result, PostType.ROLE)?.let { message ->
                _homeScreenUiEvent.emit(message)
            }
        }
    }

    fun reportPost(postType: PostType, postId: String) {
        viewModelScope.launch {
            _reportPostUiState.value = UiState.Loading
            try {
                val userData = userManager.userData
                    .filter { it.userId.isNotEmpty() }
                    .first()
                
                val result = viewPostHandlerFactory.getHandler(postType)
                    .reportPost(
                        config = RepostPostConfig(
                            postType = postType,
                            reportedBy = userData.userId,
                            postId = postId
                        )
                    )
                _reportPostUiState.value = when(result) {
                    ViewPostResult.Failure -> UiState.Error("Something went wrong")
                    ViewPostResult.Reported -> UiState.Success(Unit)
                    else -> UiState.Idle
                }
            } catch (e: Exception) {
                _reportPostUiState.value = UiState.Error("Something went wrong")
            }
        }
    }

    fun resetReportPostState() {
        _reportPostUiState.value = UiState.Idle
    }

    fun saveVacancy(vacancyDetails: VacancyDetails) {
        startOperation {
            val data = userData.filterNotNull().first()
            val result = viewPostHandlerFactory.getHandler(PostType.VACANCY)
                .savePost(config = SavePostConfig(
                    postDto = vacancyDetails,
                    savedBy = data.userId
                ))
            getSavePostMessage(result, PostType.VACANCY)?.let { message ->
                _homeScreenUiEvent.emit(message)
            }
        }
    }


    fun observeRolesInRealTime() {
        Log.d("Roles", "ViewModel Observing roles in real-time")

        viewModelScope.launch {
            homeScreenRepository.observeRoles().collect {
                _rolesUiState.value = it
            }
        }
    }


    // vacancy section


    fun observeVacancyInRealTime() {
        startOperation {
            _isVacancyLoading.value = true
            delay(700)
            Log.d("Vacancy", "ViewModel Observing new roles user refresh")
            homeScreenRepository.observeVacancy()
                .collect {
                    _vacancyUiState.value = it
                }
        }

    }
    // project section


    fun logoutUser(onLogoutSuccess: () -> Unit) {
        startOperation {
            userManager.clearUserData()
            firebaseAuth.signOut()
            onLogoutSuccess()
        }
    }


    fun fetchUserData() {
        startOperation {
            _userData.value = userManager.userData.first()
            _FCMToken.value = userManager.fcmToken.first()
            Log.d("FCM", "FCM fetched in viewmodel ${_FCMToken.value} <-")
            // getUserImageUrl()
        }
    }

    fun observeCurrentUserImage() {
        startOperation {
            userData.filterNotNull().collectLatest {
                homeScreenRepository.observeCurrentUserImage(it.userId).catch {

                }.collect {
                    _currentUserImage.value = it ?: ""
                }
            }
        }
    }

    fun saveFCMToken() {
        startOperation {
            userManager.userData
                .combine(userManager.fcmToken) { userData, token -> Pair(userData, token) }
                .filter { (userData, token) ->
                    userData.userId.isNotBlank() && token.isNotBlank()
                }
                .first()
                .let { (userData, token) ->
                    Log.d("FCM", "Saving FCM token for user ${userData.userId}")
                    homeScreenRepository.saveFcmToken(token, userData.userId)
                    Log.d("FCM", "FCM token saved successfully")
                }
        }
    }


    private fun getSavePostMessage(result: ViewPostResult, postType: PostType): String? {
        return when (result) {
            ViewPostResult.Saved -> {
                when (postType) {
                    PostType.PROJECT -> "Project Saved Successfully"
                    PostType.ROLE -> "Role Saved Successfully"
                    PostType.VACANCY -> "Vacancy Saved Successfully"
                }
            }
            ViewPostResult.Failure -> "Something went wrong"
            ViewPostResult.Unsaved -> null // Here no message needed for unsaved
            ViewPostResult.Reported -> null // Here no message needed for reported
        }
    }

}