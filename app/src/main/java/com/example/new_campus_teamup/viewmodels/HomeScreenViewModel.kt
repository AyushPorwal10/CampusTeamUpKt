package com.example.new_campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.myactivities.UserData
import com.example.new_campus_teamup.myactivities.UserManager
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.myrepository.HomeScreenRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val homeScreenRepository: HomeScreenRepository,
    private val userManager: UserManager,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> get() = _userData.asStateFlow()

    private val _userImage = MutableStateFlow<String>("")
    val userImage: StateFlow<String> = _userImage.asStateFlow()

    private val _FCMToken = MutableStateFlow<String>("")
    val FCMToken: StateFlow<String> get() = _FCMToken.asStateFlow()


    // role
    private val _rolesStateFlow = MutableStateFlow<List<RoleDetails>>(emptyList())
    val rolesStateFlow: StateFlow<List<RoleDetails>> get() = _rolesStateFlow

    private var lastVisibleRole: DocumentSnapshot? = null
    private var lastRolePostedOn: String = ""


    private val _isRoleRefreshing = MutableStateFlow<Boolean>(false)
    val isRoleRefreshing: StateFlow<Boolean> = _isRoleRefreshing

    private val _isRoleLoading = MutableStateFlow(false)
    val isRoleLoading: StateFlow<Boolean> = _isRoleLoading.asStateFlow()


    private val _isVacancyLoading = MutableStateFlow(false)
    val isVacancyLoading: StateFlow<Boolean> = _isVacancyLoading.asStateFlow()

    // vacancy
    private val _vacancyStateFlow = MutableStateFlow<List<VacancyDetails>>(emptyList())
    val vacancyStateFlow: StateFlow<List<VacancyDetails>> get() = _vacancyStateFlow .asStateFlow()

    private val _isVacancyRefreshing = MutableStateFlow<Boolean>(false)
    val isVacancyRefreshing: StateFlow<Boolean> = _isVacancyRefreshing

    // projects

    private val _listOfAllProjects = MutableStateFlow<List<ProjectDetails>>(emptyList())
    val listOfAllProjects: StateFlow<List<ProjectDetails>> = _listOfAllProjects

    private val _isProjectRefreshing = MutableStateFlow<Boolean>(false)
    val isProjectRefreshing: StateFlow<Boolean> = _isProjectRefreshing

    private val _isProjectLoading = MutableStateFlow(false)
    val isProjectLoading: StateFlow<Boolean> = _isProjectLoading

    private var lastProject: DocumentSnapshot? = null
    private var lastFetchedUserPhoneNumber: String? = null
    private var lastVisibleVacancy: DocumentSnapshot? = null
    private var lastVacancyPostedOn: String = ""

    private val _listOfSavedPost = MutableStateFlow<List<String>>(emptyList())
    val listOfSavedPost: StateFlow<List<String>> get() = _listOfSavedPost.asStateFlow()


    private val _listOfSavedRoles = MutableStateFlow<List<String>>(emptyList())
    val listOfSavedRoles: StateFlow<List<String>> get() = _listOfSavedRoles.asStateFlow()

    private val _listOfSavedVacancy = MutableStateFlow<List<String>>(emptyList())
    val listOfSavedVacancy: StateFlow<List<String>> get() = _listOfSavedVacancy.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // this is to fetch initial projects when screen loads

    init {
        fetchProjects()
        observeUserData()
    }

    fun startOperation(block : suspend () -> Unit  ){
        viewModelScope.launch {
            try{
                block()
            }
            catch (e : Exception){
                Log.d("ErrorDetected","Error detected ${e.message}")
                _errorMessage.value = "An unexpected error occurred"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun observeUserData(){
        startOperation{
            userData.collectLatest { data ->
                val phone = data?.phoneNumber
                if (phone != null && phone != lastFetchedUserPhoneNumber) {
                    lastFetchedUserPhoneNumber = phone
                    Log.d("HomeScreenVM", "User: ${data.userId} (${data.phoneNumber}) ${data.userName}")
                    fetchSavedData(phone)
                }
            }
        }
    }

    private fun fetchSavedData(phoneNumber: String) {
        viewModelScope.launch {
            launch {
                homeScreenRepository.fetchCurrentUserSavedPost(phoneNumber).catch {
                    Log.d("SavedList", "Error fetching saved list ${it}")
                }.collect {
                    _listOfSavedPost.value = it
                    Log.d("SavedList", " fetched  saved list viewmodel")
                }
            }

            launch {
                homeScreenRepository.fetchCurrentUserSavedRole(phoneNumber).catch {
                    Log.d("FetchedRole", "Error fetching saved list ${it}")
                }.collect {
                    _listOfSavedRoles.value = it

                    Log.d("FetchedRole", " fetched  saved list viewmodel")
                }
            }
            launch {
                homeScreenRepository.fetchCurrentUserSavedVacancy(phoneNumber).catch {
                    Log.d("FetchedVacancy", "Error fetching saved list $it")
                }.collect {
                    _listOfSavedVacancy.value = it

                    Log.d("FetchedVacancy", " fetched  saved list viewmodel")
                }
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
            }
            else {
                _isProjectLoading.value = false
            }
        }
    }


    // when current user wants to save a specific project

    fun saveProject(
        projectDetails: ProjectDetails,
        onProjectSaved: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        startOperation {
            userData.collectLatest { data ->
                val newPhoneNumber = data?.phoneNumber
                Log.d("ProjectSaved", "ViewModel current user id is $newPhoneNumber <-")
                if (newPhoneNumber != null) {
                    homeScreenRepository.saveProject(
                        newPhoneNumber,
                        projectDetails,
                        onProjectSaved = {
                            Log.d("ProjectSaved", "ViewModel Role saved ")
                            onProjectSaved()
                        },
                        onError = {
                            onError(it)
                        })
                }
            }
        }
    }


    fun saveRole(roleDetails: RoleDetails, onRoleSaved: () -> Unit, onError: (Exception) -> Unit) {

        startOperation{
            userData.collectLatest { data ->
                val newPhoneNumber = data?.phoneNumber
                Log.d("SaveRole", "Viewmodle current user id is $newPhoneNumber <-")
                if (newPhoneNumber != null) {
                    homeScreenRepository.saveRole(newPhoneNumber, roleDetails, onRoleSaved = {
                        Log.d("SaveRole", "Viewmodel Role saved ")
                        onRoleSaved()
                    }, onError = {
                        Log.d("SaveRole","Error saving role")
                        onError(it)
                    })
                }
                else {
                    Log.d("SaveRole","Something is wrong here")
                }
            }
        }
    }


    fun saveVacancy(
        vacancyDetails: VacancyDetails,
        onVacancySaved: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        startOperation {
            userData.collectLatest { data ->
                val newPhoneNumber = data?.phoneNumber
                Log.d("VacancySaved", "ViewModle current user id is $newPhoneNumber <-")
                if (newPhoneNumber != null) {
                    homeScreenRepository.saveVacancy(
                        newPhoneNumber,
                        vacancyDetails,
                        onVacancySaved = {
                            Log.d("Vacancy", "ViewModel vacancy saved ")
                            onVacancySaved()
                        },
                        onError = {
                            onError(it)
                        })
                }
                else {
                    Log.d("Vacancy", "SOmething is wrong here")
                }
            }
        }
    }


    fun fetchInitialOrPaginatedRoles() {
        Log.d("Roles", "ViewModel Going to fetch Initial roles")
       startOperation {
            try {

                _isRoleLoading.value = true
                val snapshot = withContext(Dispatchers.IO) {
                    homeScreenRepository.fetchInitialOrPaginatedRoles(null)
                }
                val roles = snapshot.documents.mapNotNull { it.toObject(RoleDetails::class.java) }

                if (roles.isNotEmpty()) {
                    lastVisibleRole = snapshot.documents.last()
                    lastRolePostedOn =
                        roles.first().postedOn  // because i sort it in descending order
                }
                _rolesStateFlow.value = roles
            } catch (e: Exception) {
                Log.e("Roles", "Error in loading roles $e")
            } finally {
                _isRoleLoading.value = false
            }

        }
    }


    fun observeRolesInRealTime() {
        Log.d("Roles", "ViewModel Observing roles in real-time")
        startOperation {
            _isRoleLoading.value = true
            delay(700)
            try {

                homeScreenRepository.observeRoles(
                    lastVisible = null,
                    onUpdate = { roles, newLastVisible ->
                        if (roles.isNotEmpty()) {
                            lastVisibleRole = newLastVisible
                            lastRolePostedOn = roles.first().postedOn
                            _rolesStateFlow.value = roles
                        } else {
                            _rolesStateFlow.value = emptyList()
                        }
                    },
                    onError = { error ->
                        Log.e("Roles", "Error observing real-time roles: $error")
                    })

            } finally {
                _isRoleLoading.value = false
            }


        }
    }


    // vacancy section

    fun fetchInitialOrPaginatedVacancy() {

       startOperation{
            try {
                _isVacancyLoading.value = true
                val snapshot =
                    homeScreenRepository.fetchInitialOrPaginatedVacancy(lastVisibleVacancy)
                val vacancy =
                    snapshot.documents.mapNotNull { it.toObject(VacancyDetails::class.java) }
                Log.d("SizeOfVacancy", "${vacancy.size}")

                if (vacancy.isNotEmpty()) {
                    lastVisibleVacancy = snapshot.documents.last()
                    lastVacancyPostedOn = vacancy.first().postedOn
                }

                _isVacancyLoading.value = false

                _vacancyStateFlow.value = vacancy
            } catch (e: Exception) {
                _isVacancyLoading.value = false
                Log.e("Vacancy", "$e")
            }
        }
    }


    fun observeVacancyInRealTime() {
       startOperation {
            _isVacancyLoading.value = true
            delay(700)
            Log.d("Vacancy", "ViewModel Observing new roles user refresh")
            homeScreenRepository.observeVacancy(lastVisible = null,
                onUpdate = { vacancy, newLastVisible ->
                    if (vacancy.isNotEmpty()) {
                        lastVisibleVacancy = newLastVisible
                        lastRolePostedOn = vacancy.first().postedOn
                        _vacancyStateFlow.value = vacancy
                    } else {
                        _vacancyStateFlow.value = emptyList()
                        Log.d("Vacancy", "ViewModel Observing new roles got empty on  refresh")
                    }
                    _isVacancyLoading.value = false
                },
                onError = { error ->
                    _isVacancyLoading.value = false
                    Log.e("Vacancy", "$error")
                })

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
            getUserImageUrl()
        }
    }

    private fun getUserImageUrl() {
        startOperation {
            userManager.userData
                .filter { it.userId.isNotEmpty() }
                .first()
                .let {
                    homeScreenRepository.getUserImageUrl(it.userId).collect { imageUrl ->
                        _userImage.value = imageUrl
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

}