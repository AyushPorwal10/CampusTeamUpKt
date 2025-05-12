package com.example.new_campus_teamup.yourposts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.myactivities.UserManager
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YourPostViewModel @Inject constructor(
    private val userManager: UserManager,
    private val yourPostRepo: YourPostRepo
) : ViewModel() {

    val tag = "YourPost"


    private val _postedRoles = MutableStateFlow<List<RoleDetails>>(emptyList())
    val postedRoles: StateFlow<List<RoleDetails>> get() = _postedRoles.asStateFlow()


    private val _postedVacancy = MutableStateFlow<List<VacancyDetails>>(emptyList())
    val postedVacancy: StateFlow<List<VacancyDetails>> get() = _postedVacancy.asStateFlow()


    private val _postedProjects = MutableStateFlow<List<ProjectDetails>>(emptyList())
    val postedProjects: StateFlow<List<ProjectDetails>> get() = _postedProjects.asStateFlow()


    fun fetchUserPostedRoles() {
        viewModelScope.launch {
            userManager.userData.collectLatest {
                Log.d(tag, "Phone Number ${it.phoneNumber} , Going to fetch roles")

                 yourPostRepo.observeUserPostedRoles(it.phoneNumber).catch {
                     Log.d(tag , "Error fetching roles")
                }.collect{
                     _postedRoles.value = it
                }
                Log.d(tag, "Fetched Size roles list is ${postedRoles.value.size}")
            }
        }
    }

    fun fetchUserPostedVacancy() {
        viewModelScope.launch {
            userManager.userData.collectLatest {
                Log.d(tag, "Phone Number ${it.phoneNumber} , Going to fetch vacancy")
                yourPostRepo.observeUserPostedVacancy(it.phoneNumber).catch {
                    Log.d(tag , "Error fetching vacancy")
                }
                    .collect{
                        _postedVacancy.value =it
                    }
                Log.d(tag, "Fetched Size vacancy list is ${postedVacancy.value.size}")
            }
        }
    }

    fun fetchUserPostedProjects() {
        viewModelScope.launch {
            userManager.userData.collectLatest {
                Log.d(tag, "Phone Number ${it.phoneNumber} , Going to fetch projects")

                yourPostRepo.observeUserPostedProjects(it.phoneNumber)
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
        viewModelScope.launch {
            userManager.userData.collectLatest {
                yourPostRepo.deleteRole(roleId, it.phoneNumber, onRoleDelete = {
                    onDelete()
                }, onError = {
                    onError()
                })
            }
        }
    }

    fun deleteVacancy(vacancyId: String, onDelete: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            userManager.userData.collectLatest {
                yourPostRepo.deleteVacancy(vacancyId, it.phoneNumber, onVacancyDelete = {
                    onDelete()
                }, onError = {
                    onError()
                })
            }
        }
    }

    fun deleteProject(projectId: String, onDelete: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            userManager.userData.collectLatest {
                yourPostRepo.deleteProject(projectId, it.phoneNumber, onProjectDelete = {
                    onDelete()
                }, onError = {
                    onError()
                })
            }
        }
    }
}