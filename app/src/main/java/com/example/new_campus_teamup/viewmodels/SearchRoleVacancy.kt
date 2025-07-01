package com.example.new_campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.helper.CheckNetworkConnectivity
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.myrepository.SearchRoleVacancyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchRoleVacancy @Inject constructor(
    private val searchRoleVacancyRepository: SearchRoleVacancyRepository,
    private val networkMonitor: CheckNetworkConnectivity
) : ViewModel() {

    private val _searchRoleText = MutableStateFlow("")
    val searchRoleText = _searchRoleText.asStateFlow()
    private val _isRoleSearching = MutableStateFlow(false)
    val isRoleSearching: StateFlow<Boolean> = _isRoleSearching
    private val _searchedRoles = MutableStateFlow<List<RoleDetails>>(emptyList())
    val searchedRoles: StateFlow<List<RoleDetails>> get() = _searchedRoles


    private val _searchVacancyText = MutableStateFlow("")
    val searchVacancyText = _searchVacancyText.asStateFlow()

    private val _isVacancySearching = MutableStateFlow(false)
    val isVacancySearching = _isVacancySearching.asStateFlow()

    private val _searchedVacancies = MutableStateFlow<List<VacancyDetails>>(emptyList())
    val searchedVacancies: StateFlow<List<VacancyDetails>> get() = _searchedVacancies


    init {
        observeRoleSearchQuery()
        observeVacancySearchQuery()
    }


    private fun observeRoleSearchQuery() {
        viewModelScope.launch {
            searchRoleText.debounce(500) // Wait for user to stop typing
                .collect { query ->
                    if (query.isNotBlank()) {
                        fetchRolesFromFirebase(query)
                    } else {
                        _searchedRoles.value = emptyList() // Clear search when empty
                    }
                }
        }
    }

    private fun fetchRolesFromFirebase(query: String) {
        _isRoleSearching.value = true

        viewModelScope.launch {
            searchRoleVacancyRepository.fetchRolesFromFirebase(query, onSearched = { filteredRole ->
                if (filteredRole.isNotEmpty()) {
                    _searchedRoles.value = filteredRole
                } else {
                    _searchedRoles.value = emptyList()
                }

                _isRoleSearching.value = false
            },
                onError = { exception ->
                    _isRoleSearching.value = false
                    Log.e("SearchRoleVacancy", "Error fetching roles", exception)
                })
        }
    }

    fun onSearchRoleTextChange(text: String) {
        _isRoleSearching.value = true
        _searchRoleText.value = text
    }


    private fun observeVacancySearchQuery() {
        viewModelScope.launch {
            searchVacancyText.debounce(500)
                .collect { query ->
                    if (query.isNotEmpty()) {
                        fetchVacancyFromFirebase(query)
                    } else {
                        _searchedVacancies.value = emptyList()
                    }
                }
        }
    }

    private fun fetchVacancyFromFirebase(query: String) {
        viewModelScope.launch {

            searchRoleVacancyRepository.fetchVacancyFromFirebase(query,
                onSearched = { filteredVacancies ->

                    if (filteredVacancies.isNotEmpty())
                        _searchedVacancies.value = filteredVacancies
                    else
                        _searchedVacancies.value = emptyList()

                    _isVacancySearching.value = false
                },
                onError = { exception ->
                    Log.d("SearchRoleVacancy", exception.toString())
                })
        }
    }

    fun onSearchedVacancyTextChange(text: String) {
        _isVacancySearching.value = true
        _searchVacancyText.value = text
    }
}

