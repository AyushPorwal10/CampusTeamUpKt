package com.example.campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.myrepository.TeamDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailsViewModel @Inject constructor(
    private val teamDetailsRepository: TeamDetailsRepository,
    private val userManager: UserManager
) : ViewModel() {



    private val _userId = MutableStateFlow<String>("")
    val userId : StateFlow<String>  =  _userId.asStateFlow()

    private val _collegeName = MutableStateFlow<String>("")
    val collegeName : StateFlow<String> = _collegeName.asStateFlow()

    private val _searchUserNameText = MutableStateFlow<String>("")
    val searchUserNameText = _searchUserNameText.asStateFlow()

    private val _listOfUserName = MutableStateFlow<List<String>>(emptyList())
    val listOfUserName = _listOfUserName.asStateFlow()

    private val _isTeamDetailsSaving = MutableStateFlow<Boolean>(false)
    val isTeamDetailsSaving : StateFlow<Boolean> get() = _isTeamDetailsSaving.asStateFlow()


    private val _teamMemberList = MutableStateFlow<List<String>>(emptyList())
    val teamMemberList : StateFlow<List<String>>get() = _teamMemberList.asStateFlow()


    init {
        observeUserNameQuery()
        fetchTeamDetails()
    }


    fun initializeUserId() {
        viewModelScope.launch {
            userManager.userData
                .filter { it.userId.isNotEmpty()}
                .first()
                .let {
                    Log.d("TeamDetailsUserId","User id is initialized with : ${it.userId}")
                    _userId.value = it.userId
                    _collegeName.value = it.collegeName
                }
        }
    }

    fun addTeamMember(member : String){
        _teamMemberList.value = _teamMemberList.value + member
    }

    fun updateTeamMember(index: Int , newUserName : String){

        _teamMemberList.value = _teamMemberList.value.toMutableList().apply {
            if(index in indices ){
                this[index] = newUserName
            }
        }
    }

    fun removeTeamMember(index: Int){
        _teamMemberList.value = _teamMemberList.value.toMutableList().apply {
            if(index in indices){
                removeAt(index)
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
        listOfTeamMembers: List<String>,
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

    fun saveTeamDetails(listOfTeamMembers:List<String>) {
        viewModelScope.launch {

            _isTeamDetailsSaving.value= true
            teamDetailsRepository.saveTeamDetails(collegeName.value.lowercase() , listOfTeamMembers, _userId.value) {
                _isTeamDetailsSaving.value = false
            }
        }
    }

     fun fetchTeamDetails(){

        viewModelScope.launch {
            _isTeamDetailsSaving.value = true
            Log.d("TeamDetailsUserId","Fetch team details with ${_userId.value}")
            userManager.userData
                .filter { it.userId.isNotEmpty() }
                .first()
                .let {
                    Log.d("TeamDetailsUserId","Inside let it is ${it.userId}")
                    _teamMemberList.value  =   teamDetailsRepository.fetchTeamDetails(it.userId)
                    _isTeamDetailsSaving.value = false
                }
        }
    }
}