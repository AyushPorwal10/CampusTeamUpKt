package com.example.campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.myrepository.ViewVacancyRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewVacancyViewModel @Inject constructor(
    private val viewVacancyRepository: ViewVacancyRepository
) : ViewModel() {


    private val _teamDetails = MutableStateFlow<List<String>>(emptyList())

    val teamDetails : StateFlow<List<String>> = _teamDetails.asStateFlow()

    val _userIdWithMap = MutableStateFlow<Map<String , String>>(emptyMap())
    val userIdWithMap : StateFlow<Map<String, String>> get() = _userIdWithMap.asStateFlow()


    fun fetchTeamDetails(postedBy : String){
        viewModelScope.launch {
            viewVacancyRepository.fetchTeamDetails(postedBy).collect{
                Log.d("Testing","Size of teamUserId in viewmodel is ${it.size}")
                _teamDetails.value = it
                fetchMemberImage()
            }
        }
    }

    private fun fetchMemberImage(){
        viewModelScope.launch {

            _userIdWithMap.value  =  viewVacancyRepository.fetchMemberImage(_teamDetails.value)
            Log.d("Testing","Size of map in viewmodel is ${userIdWithMap.value.size}")
            Log.d("Userid","Map size is ${_userIdWithMap.value.size}")
        }
    }
}