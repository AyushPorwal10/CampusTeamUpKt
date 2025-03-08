package com.example.campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.myactivities.UserData
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.mydataclass.ProjectDetails
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.example.campus_teamup.myrepository.HomeScreenRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val homeScreenRepository: HomeScreenRepository,
    private val userManager: UserManager,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {




    private val _userData = MutableStateFlow<UserData?>(null)
    val userData : StateFlow<UserData?> get() = _userData.asStateFlow()

    private val _userImage = MutableStateFlow<String>("")
    val userImage : StateFlow<String> = _userImage.asStateFlow()

    private val _FCMToken = MutableStateFlow<String>("")
    val FCMToken : StateFlow<String> get() = _FCMToken.asStateFlow()


    // role
    private val _rolesStateFlow = MutableStateFlow<List<RoleDetails>>(emptyList())
    val rolesStateFlow: StateFlow<List<RoleDetails>> get() = _rolesStateFlow

    private var lastVisibleRole: DocumentSnapshot? = null
    private var lastRolePostedOn: String = ""

    private val _isRoleRefreshing = MutableStateFlow<Boolean>(false)
    val isRoleRefreshing: StateFlow<Boolean> = _isRoleRefreshing

    private val _isRoleLoading = MutableStateFlow(false)
    val isRoleLoading: StateFlow<Boolean> = _isRoleLoading


    // vacancy
    private val _vacancyStateFlow = MutableStateFlow<List<VacancyDetails>>(emptyList())
    val vacancyStateFlow: StateFlow<List<VacancyDetails>> get() = _vacancyStateFlow // find why we use get here

    private var lastVisibleVacancy: DocumentSnapshot? = null
    private var lastVacancyPostedOn: String = ""

    private val _isVacancyRefreshing = MutableStateFlow<Boolean>(false)
    val isVacancyRefreshing: StateFlow<Boolean> = _isVacancyRefreshing


    // projects

    private val _projectStateFlow = MutableStateFlow<List<ProjectDetails>>(emptyList())
    val projectFlow: StateFlow<List<ProjectDetails>> = _projectStateFlow

    private val _isProjectRefreshing = MutableStateFlow<Boolean>(false)
    val isProjectRefreshing: StateFlow<Boolean> = _isProjectRefreshing

    private val _isProjectLoading = MutableStateFlow(false)
    val isProjectLoading: StateFlow<Boolean> = _isProjectLoading


    fun fetchInitialOrPaginatedRoles() {
        Log.d("Roles", "ViewModel Going to fetch Initial roles")
        viewModelScope.launch {
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


    fun loadMoreRoles() {
        Log.d("Roles", "ViewModel Loading more roles")
        viewModelScope.launch {
            try {
                val snapshot = homeScreenRepository.fetchInitialOrPaginatedRoles(lastVisibleRole)
                val roles = snapshot.documents.mapNotNull { it.toObject(RoleDetails::class.java) }

                if (roles.isNotEmpty()) {

                    lastVisibleRole = snapshot.documents.last()
                } else {
                    Log.d("Roles", "Load more roles got empty")
                }
                _rolesStateFlow.value = (_rolesStateFlow.value ?: emptyList()) + roles
            } catch (e: Exception) {
                Log.e("Roles", "Error loading more roles $e")
            }

        }
    }

    fun observeRolesInRealTime() {
        Log.d("Roles", "ViewModel Observing roles in real-time")
        viewModelScope.launch {
            _isRoleLoading.value = true
            try {
                homeScreenRepository.observeRoles(
                    lastVisible = null,
                    onUpdate = { roles, newLastVisible ->
                        if (roles.isNotEmpty()) {
                            lastVisibleRole = newLastVisible
                            lastRolePostedOn = roles.first().postedOn
                            _rolesStateFlow.value = roles
                        }
                        else{
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

        viewModelScope.launch {
            try {
                val snapshot =
                    homeScreenRepository.fetchInitialOrPaginatedVacancy(lastVisibleVacancy)
                val vacancy =
                    snapshot.documents.mapNotNull { it.toObject(VacancyDetails::class.java) }

                if (vacancy.isNotEmpty()) {
                    lastVisibleVacancy = snapshot.documents.last()
                    lastVacancyPostedOn = vacancy.first().postedOn
                }

                _vacancyStateFlow.value = vacancy
            } catch (e: Exception) {
                Log.e("Vacancy", "$e")
            }
        }
    }


    fun observeVacancyInRealTime() {
        viewModelScope.launch {
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
                },
                onError = { error ->
                    Log.e("Vacancy", "$error")
                })

        }

    }
    // project section

    fun fetchProjects() {
        viewModelScope.launch {

            try {
                Log.d("Project", "Going to fetch project")
                withContext(Dispatchers.IO) {
                    val snapshot = homeScreenRepository.fetchPaginatedProjects()
                    val projects = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(ProjectDetails::class.java)
                    }

                    _projectStateFlow.value = projects
                }
            } finally {

            }
        }
    }

    fun observeProjectInRealTime() {
        viewModelScope.launch {
            _isProjectRefreshing.value = true
            Log.d("Project", "Is refreshing become true")

            try {
                homeScreenRepository.observeProjects(
                    onError = {
                        Log.e("Project", it.toString())
                        _isProjectRefreshing.value = false
                        Log.d("Project", "ON ERROR  Is refreshing become false")
                    },
                    onUpdate = { updatedProjects ->
                        _isProjectRefreshing.value = false

                        if (updatedProjects.isNotEmpty()) {
                            _projectStateFlow.value = updatedProjects
                        } else {
                            _projectStateFlow.value = emptyList()
                            Log.d("Project", "Project got empty when refreshed")
                        }
                        Log.d("Project", " ONUPDATE Is refreshing become false")
                    }
                )
            } finally{
                _isProjectRefreshing.value = false
            }
        }
    }

    fun logoutUser(onLogoutSuccess : ()->Unit){
        viewModelScope.launch {
            userManager.clearUserData()
            firebaseAuth.signOut()
            onLogoutSuccess()
        }
    }


    fun fetchUserData(){
        viewModelScope.launch {
           _userData.value = userManager.userData.first()
            _FCMToken.value = userManager.fcmToken.first()
            Log.d("FCM","FCM fetched in viewmodel ${_FCMToken.value}")
            getUserImageUrl()
        }
    }

    private fun getUserImageUrl(){
        viewModelScope.launch {

            _userImage.value = homeScreenRepository.getUserImageUrl(userData.value?.userId)
            Log.d("Profile","${_userImage.value} is the userimage")
        }
    }

    fun saveFCMToken(){
        viewModelScope.launch {
            userData.value?.userId?.let { homeScreenRepository.saveFcmToken( FCMToken.value, it) }
        }
    }

}