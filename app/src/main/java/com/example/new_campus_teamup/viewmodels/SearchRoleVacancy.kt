package com.example.new_campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.UiState
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
    private val _searchedRolesUiState = MutableStateFlow<UiState<List<RoleDetails>>>(UiState.Idle)
    val searchedRolesUiState: StateFlow<UiState<List<RoleDetails>>> get() = _searchedRolesUiState


    private val _searchVacancyText = MutableStateFlow("")
    val searchVacancyText = _searchVacancyText.asStateFlow()

    private val _isVacancySearching = MutableStateFlow(false)
    val isVacancySearching = _isVacancySearching.asStateFlow()

    private val _searchedVacanciesUiState = MutableStateFlow<UiState<List<VacancyDetails>>>(UiState.Idle)
    val searchedVacanciesUiState: StateFlow<UiState<List<VacancyDetails>>> get() = _searchedVacanciesUiState


    init {
        observeRoleSearchQuery()
        observeVacancySearchQuery()
    }


    private fun observeRoleSearchQuery() {
        viewModelScope.launch {
            searchRoleText.debounce(500)
                .collect { query ->
                    if (query.isNotBlank()) {
                        fetchRolesFromFirebase(query)
                    } else {
                        _searchedRolesUiState.value = UiState.Success(emptyList())
                    }
                }
        }
    }

    private fun fetchRolesFromFirebase(query: String) {

        viewModelScope.launch {
            searchRoleVacancyRepository.fetchRolesFromFirebase(query , onStateChange = {
              _searchedRolesUiState.value = it
            })
        }
    }

    fun onSearchRoleTextChange(text: String) {
        _searchRoleText.value = text
    }


    private fun observeVacancySearchQuery() {
        viewModelScope.launch {
            searchVacancyText.debounce(500)
                .collect { query ->
                    if (query.isNotEmpty()) {
                        fetchVacancyFromFirebase(query)
                    } else {
                        _searchedVacanciesUiState.value = UiState.Success(emptyList())
                    }
                }
        }
    }

    private fun fetchVacancyFromFirebase(query: String) {
        viewModelScope.launch {

            searchRoleVacancyRepository.fetchVacancyFromFirebase(query, onStateChange = {
                _searchedVacanciesUiState.value = it
            })
        }
    }

    fun onSearchedVacancyTextChange(text: String) {
        _isVacancySearching.value = true
        _searchVacancyText.value = text
    }
}

