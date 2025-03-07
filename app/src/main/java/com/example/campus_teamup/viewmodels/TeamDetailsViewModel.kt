package com.example.campus_teamup.viewmodels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.myrepository.TeamDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailsViewModel @Inject constructor(
    private val teamDetailsRepository: TeamDetailsRepository,
    private val userManager: UserManager
) : ViewModel() {



    private val _userId = MutableStateFlow<String>("")
    val userId = _userId.asStateFlow()

    private val _collegeName = MutableStateFlow<String>("")
    val collegeName : StateFlow<String> = _collegeName.asStateFlow()

    private val _searchUserNameText = MutableStateFlow<String>("")
    val searchUserNameText = _searchUserNameText.asStateFlow()

    private val _listOfUserName = MutableStateFlow<List<String>>(emptyList())
    val listOfUserName = _listOfUserName.asStateFlow()

    init {
        observeUserNameQuery()
    }


    fun initializeUserId() {
        viewModelScope.launch {
            userManager.userData.collect {
                _userId.value = it.userId
                _collegeName.value = it.collegeName

            }
        }
    }

    private fun observeUserNameQuery() {
        viewModelScope.launch {
            searchUserNameText.debounce(400)
                .collect { query ->
                    if (query.isNotEmpty()) {
                        fetchUserNames(query)
                    } else {
                        _listOfUserName.value = emptyList()
                    }
                }
        }
    }

    fun onSearchTextChange(queryText: String) {
        Log.d("UserName", "Search text updated $_searchUserNameText")
        _searchUserNameText.value = queryText
    }

    fun fetchUserNames(query: String) {

        viewModelScope.launch {
            teamDetailsRepository.fetchUserName(query, onUpdate = { list ->
                Log.d("UserName", "Going to fetch username for $query")
                if (list.isNotEmpty()) {
                    Log.d("UserName", "Search List size ${list.size}")
                    _listOfUserName.value = list
                } else {
                    Log.d("UserName", "Search List comes empty")
                    _listOfUserName.value = emptyList()
                }
            },
                onError = { exception ->
                    _listOfUserName.value = emptyList()
                    Log.e("UserName", exception.toString())
                })
        }
    }


    fun checkIfUserInOtherTeam(
        listOfTeamMembers: SnapshotStateList<String>,
        isPresent: (Boolean) -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            teamDetailsRepository.checkIfUserInOtherTeam(collegeName.value, listOfTeamMembers, isPresent = {
                isPresent(it)
            },
                onError = {
                    onError()
                })
        }

    }

    fun saveTeamDetails(listOfTeamMembers: SnapshotStateList<String>) {

        viewModelScope.launch {
            teamDetailsRepository.saveTeamDetails(collegeName.value , listOfTeamMembers, _userId.value)
        }
    }

    suspend fun fetchTeamDetails(): List<String> {
        return teamDetailsRepository.fetchTeamDetails(_userId.value)
    }
}