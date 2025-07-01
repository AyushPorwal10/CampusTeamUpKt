package com.example.new_campus_teamup.yourposts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.helper.CheckNetworkConnectivity
import com.example.new_campus_teamup.myactivities.UserManager
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class YourPostViewModel @Inject constructor(
    private val userManager: UserManager,
    private val yourPostRepo: YourPostRepo,
    private val networkMonitor: CheckNetworkConnectivity
) : ViewModel() {

    val tag = "YourPost"


    private val _postedRoles = MutableStateFlow<List<RoleDetails>>(emptyList())
    val postedRoles: StateFlow<List<RoleDetails>> get() = _postedRoles.asStateFlow()


    private val _postedVacancy = MutableStateFlow<List<VacancyDetails>>(emptyList())
    val postedVacancy: StateFlow<List<VacancyDetails>> get() = _postedVacancy.asStateFlow()


    private val _postedProjects = MutableStateFlow<List<ProjectDetails>>(emptyList())
    val postedProjects: StateFlow<List<ProjectDetails>> get() = _postedProjects.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

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

    fun fetchUserPostedRoles() {
        startOperation {
            userManager.userData.collectLatest {
                Log.d(tag, "User id  ${it.userId} , Going to fetch roles")

                 yourPostRepo.observeUserPostedRoles(it.userId).catch {
                     Log.d(tag , "Error fetching roles")
                }.collect{
                     _postedRoles.value = it
                }
                Log.d(tag, "Fetched Size roles list is ${postedRoles.value.size}")
            }
        }
    }

    fun fetchUserPostedVacancy() {
        startOperation{
            userManager.userData.collectLatest {
                Log.d(tag, "User id ${it.userId} , Going to fetch vacancy")
                yourPostRepo.observeUserPostedVacancy(it.userId).catch {
                    Log.d(tag , "Error fetching vacancy")
                }
                    .collect{
                        _postedVacancy.value =it
                    }
                Log.d(tag, "Fetched Size vacancy list is ${postedVacancy.value.size}")
            }
        }
    }
    fun clearError() {
        _errorMessage.value = null
    }
    fun fetchUserPostedProjects() {
       startOperation {
            userManager.userData.collectLatest {
                Log.d(tag, "User id ${it.userId} , Going to fetch projects")

                yourPostRepo.observeUserPostedProjects(it.userId)
                    .catch {
                        Log.d(tag , "Error fetching projects")
                    }
                    .collect{
                        _postedProjects.value =it
                    }
                Log.d(tag, "Fetched Size projects list is ${postedProjects.value.size}")
            }
        }
    }

    fun deleteRole(roleId: String, onDelete: () -> Unit, onError: () -> Unit) {
        startOperation {
            userManager.userData.collectLatest {
                yourPostRepo.deleteRole(roleId, it.userId, onRoleDelete = {
                    onDelete()
                }, onError = {
                    onError()
                })
            }
        }
    }

    fun deleteVacancy(vacancyId: String, onDelete: () -> Unit, onError: () -> Unit) {
        startOperation {
            userManager.userData.collectLatest {
                yourPostRepo.deleteVacancy(vacancyId, it.userId, onVacancyDelete = {
                    onDelete()
                }, onError = {
                    onError()
                })
            }
        }
    }

    fun deleteProject(projectId: String, onDelete: () -> Unit, onError: () -> Unit) {
        startOperation {
            userManager.userData.collectLatest {
                yourPostRepo.deleteProject(projectId, it.userId, onProjectDelete = {
                    onDelete()
                }, onError = {
                    onError()
                })
            }
        }
    }
}