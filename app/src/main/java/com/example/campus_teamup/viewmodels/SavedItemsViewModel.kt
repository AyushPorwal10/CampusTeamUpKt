package com.example.campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.mydataclass.ProjectDetails
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.example.campus_teamup.myrepository.SavedItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SavedItemsViewModel @Inject constructor(
    private val savedItemsRepository: SavedItemsRepository
) : ViewModel() {

    private val _savedProjectList = MutableStateFlow<List<ProjectDetails>>(emptyList())

    val showProjectList: StateFlow<List<ProjectDetails>> get() = _savedProjectList.asStateFlow()

    private val _savedRolesList = MutableStateFlow<List<RoleDetails>>(emptyList())

    val savedRolesList: StateFlow<List<RoleDetails>> get() = _savedRolesList.asStateFlow()

    private val _savedVacancyList = MutableStateFlow<List<VacancyDetails>>(emptyList())

    val savedVacancyList: StateFlow<List<VacancyDetails>> get() = _savedVacancyList.asStateFlow()


    // keep in mind when user click on view roles or other ensure to check whether it exists for not


    // START USING GENERIC CLASS CONCEPT

    fun fetchSavedProjects(phoneNumber: String?) {

        Log.d("FetchingProjects", "Current user id is $phoneNumber")
        viewModelScope.launch {
            if (phoneNumber != null) {
                savedItemsRepository.fetchSavedProjects(phoneNumber).catch {
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

    fun fetchSavedRoles(phoneNumber: String?) {
        Log.d("FetchingRoles", "Current user id is $phoneNumber")
        viewModelScope.launch {
            if (phoneNumber != null) {
                savedItemsRepository.fetchSavedRoles(phoneNumber).catch {
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

    fun fetchSavedVacancy(phoneNumber: String?) {
        Log.d("FetchingVacancy", "Current user id is $phoneNumber")
        viewModelScope.launch {
            if (phoneNumber != null) {
                savedItemsRepository.fetchSavedVacancies(phoneNumber).catch {
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


    fun unSaveProject(projectId: String, phoneNumber: String?) {

        if (phoneNumber != null) {
            Log.d("Unsave", "current user id is $phoneNumber")
            viewModelScope.launch {
                savedItemsRepository.unSaveProject(phoneNumber, projectId)
            }
        } else {
            Log.d("Unsave", "current user id in unsave is null")
        }
    }

    fun unSaveRole(roleId: String, phoneNumber: String?) {
        if (phoneNumber != null) {
            Log.d("UnsaveRole", "current user id is $phoneNumber")
            viewModelScope.launch {
                savedItemsRepository.unSaveRole(phoneNumber, roleId)
                Log.d("UnsaveRole", "Role unsaved")
            }
        } else {
            Log.d("UnsaveRole", "current user id is null")
        }
    }

    fun unSaveVacancy(vacancyId : String , phoneNumber: String?){
        if (phoneNumber != null) {
            Log.d("UnsaveVacancy", "current user id is $phoneNumber")
            viewModelScope.launch {
                savedItemsRepository.unSaveVacancy(phoneNumber, vacancyId)
                Log.d("UnsaveVacancy", "Role unsaved")
            }
        } else {
            Log.d("UnsaveVacancy", "current user id is null")
        }
    }



}