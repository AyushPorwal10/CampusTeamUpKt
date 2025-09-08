package com.example.new_campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.UiState
import com.example.new_campus_teamup.helper.CheckNetworkConnectivity
import com.example.new_campus_teamup.myactivities.UserManager
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.myrepository.SavedItemsRepository
import com.example.new_campus_teamup.room.RoleEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.lang.Thread.State
import javax.inject.Inject


@HiltViewModel
class SavedItemsViewModel @Inject constructor(
    private val savedItemsRepository: SavedItemsRepository,
    private val userManager: UserManager,
    private val networkMonitor: CheckNetworkConnectivity
) : ViewModel() {

    private val _savedProjectList = MutableStateFlow<List<ProjectDetails>>(emptyList())
    val showProjectList: StateFlow<List<ProjectDetails>> get() = _savedProjectList.asStateFlow()

    private val _savedRolesList = MutableStateFlow<List<RoleDetails>>(emptyList())
    val savedRolesList: StateFlow<List<RoleDetails>> get() = _savedRolesList.asStateFlow()

    private val _savedVacancyList = MutableStateFlow<List<VacancyDetails>>(emptyList())
    val savedVacancyList: StateFlow<List<VacancyDetails>> get() = _savedVacancyList.asStateFlow()


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _reportPostUiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val reportPostUiState : StateFlow<UiState<Unit>> = _reportPostUiState.asStateFlow()

    private fun startOperation(block : suspend  () -> Unit){
        viewModelScope.launch {
            if (!networkMonitor.isConnectedNow()) {
                _errorMessage.value = "No internet connection. Please retry later."
                return@launch
            }
            try {
                 block()
            } catch (toe: TimeoutCancellationException) {
                _errorMessage.value = "Request timed out. Check your connection."
            } catch (e: Exception) {
                Log.e("HomeScreenVM", "Unexpected error", e)
                _errorMessage.value = "Something went wrong. Please try again."
            }

        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    // keep in mind when user click on view roles or other ensure to check whether it exists for not
    fun fetchSavedProjects(currentUserId: String?) {

        Log.d("FetchingProjects", "Current user id is $currentUserId")
        startOperation{
            if (currentUserId != null) {
                savedItemsRepository.fetchSavedProjects(currentUserId).catch {
                    Log.d("FetchingProjects", "Error fetching projects")
                }
                    .collect {
                        _savedProjectList.value = it
                        Log.d(
                            "FetchingProjects",
                            "In Viewmodel saved projects size is ${_savedProjectList.value.size}"
                        )
                    }
            } else {
                Log.d("FetchingProjects", "Current user id is null")
            }
        }
    }

    fun fetchSavedRoles(currentUserId: String?) {
        Log.d("FetchingRoles", "Current user id is $currentUserId")
        startOperation {
            if (currentUserId != null) {
                savedItemsRepository.fetchSavedRoles(currentUserId).catch {
                    Log.d("FetchingRoles", "Error fetching roles")
                }
                    .collect {
                        _savedRolesList.value = it
                        Log.d(
                            "FetchingRoles",
                            "In Viewmodel saved roles size is ${_savedRolesList.value.size}"
                        )
                    }
            } else {
                Log.d("FetchingRoles", "Current user id is null")
            }
        }
    }

    fun fetchSavedVacancy(currentUserId: String?) {
        Log.d("FetchingVacancy", "Current user id is $currentUserId")
        startOperation{
            if (currentUserId != null) {
                savedItemsRepository.fetchSavedVacancies(currentUserId).catch {
                    Log.d("FetchingVacancy", "Error fetching roles")
                }
                    .collect {
                        _savedVacancyList.value = it
                        Log.d(
                            "FetchingVacancy",
                            "In Viewmodel saved vacancy size is ${_savedRolesList.value.size}"
                        )
                    }
            } else {
                Log.d("FetchingVacancy", "Current user id is null")
            }
        }
    }


    fun unSaveProject(projectId: String, currentUserId: String?) {

        if (currentUserId != null) {
            Log.d("Unsave", "current user id is $currentUserId")
            startOperation {
                savedItemsRepository.unSaveProject(currentUserId, projectId)
            }
        } else {
            Log.d("Unsave", "current user id in unsave is null")
        }
    }

    fun unSaveRole(roleId: String, currentUserId: String?) {
        if (currentUserId != null) {
            Log.d("UnsaveRole", "current user id is $currentUserId")
            startOperation {
                savedItemsRepository.unSaveRole(currentUserId, roleId)
                Log.d("UnsaveRole", "Role unsaved")
            }
        } else {
            Log.d("UnsaveRole", "current user id is null")
        }
    }

    fun unSaveVacancy(vacancyId : String , currentUserId: String?){
        if (currentUserId != null) {
            Log.d("UnsaveVacancy", "current user id is $currentUserId")
            startOperation {
                savedItemsRepository.unSaveVacancy(currentUserId, vacancyId)
                Log.d("UnsaveVacancy", "Role unsaved")
            }
        } else {
            Log.d("UnsaveVacancy", "current user id is null")
        }
    }

    fun reportPost(tag : String , roleId : String ){
        viewModelScope.launch {
            userManager.userData.collect{
                if(it.userId.isNotEmpty()){
                    savedItemsRepository.reportPost(tag , roleId , it.userId, onStateChange = {state->
                        _reportPostUiState.value = state
                    })
                }
            }
        }
    }

    fun resetReportPostState(){
        _reportPostUiState.value = UiState.Idle
    }


}