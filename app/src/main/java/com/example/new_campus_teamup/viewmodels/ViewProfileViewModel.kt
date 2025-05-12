package com.example.new_campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.mydataclass.CollegeDetails
import com.example.new_campus_teamup.myrepository.ViewProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewProfileViewModel @Inject constructor(
private val viewProfileRepository: ViewProfileRepository
) : ViewModel(){


    private val _collegeDetails = MutableStateFlow<CollegeDetails?>(null)
    val collegeDetails : StateFlow<CollegeDetails?> get()  = _collegeDetails.asStateFlow()

    private val _codingProfilesDetails = MutableStateFlow<List<String>>(emptyList())
    val codingProfilesDetails : StateFlow<List<String>> get()  = _codingProfilesDetails.asStateFlow()

    private val _skills = MutableStateFlow<List<String>>(emptyList())
    val skills : StateFlow<List<String>> get()  = _skills.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private fun startOperation(block : suspend  () -> Unit){
        viewModelScope.launch {
            try{
                block()
            }
            catch (e : Exception){
                _errorMessage.value = "An unexpected error occurred"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun fetchCollegeDetails(userId: String, receiverPhoneNumber: String?){
        startOperation{
            if (receiverPhoneNumber != null) {
                viewProfileRepository.fetchCollegeDetails(userId, receiverPhoneNumber).collect{details->
                    _collegeDetails.value = details
                }
            }
        }
    }

    fun fetchCodingProfileDetails(userId: String, receiverPhoneNumber: String?){
        startOperation {
            Log.d("Coding","User id is $userId")
            Log.d("Coding","Going to fetch coding profiles viewmodel")
            if (receiverPhoneNumber != null) {
                viewProfileRepository.fetchCodingProfilesDetails(userId, receiverPhoneNumber).collect{details->
                    Log.d("Coding","${details.size} updating stateflow")
                    _codingProfilesDetails.value = details
                }
            }
        }
    }

    fun fetchSkills(userId: String, receiverPhoneNumber: String?){
        startOperation {
            if (receiverPhoneNumber != null) {
                viewProfileRepository.fetchSkills(userId, receiverPhoneNumber).collect{listOfSkills->
                    _skills.value = listOfSkills
                }
            }
        }
    }
}