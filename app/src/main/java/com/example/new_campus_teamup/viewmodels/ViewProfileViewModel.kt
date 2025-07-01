package com.example.new_campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.helper.CheckNetworkConnectivity
import com.example.new_campus_teamup.mydataclass.CollegeDetails
import com.example.new_campus_teamup.myrepository.ViewProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class ViewProfileViewModel @Inject constructor(
private val viewProfileRepository: ViewProfileRepository,
private val networkMonitor: CheckNetworkConnectivity
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

    fun fetchCollegeDetails(userId: String){
        startOperation{
                viewProfileRepository.fetchCollegeDetails(userId).collect{details->
                    _collegeDetails.value = details
            }
        }
    }

    fun fetchCodingProfileDetails(userId: String){
        startOperation {
            Log.d("Coding","User id is $userId")
            Log.d("Coding","Going to fetch coding profiles viewmodel")
                viewProfileRepository.fetchCodingProfilesDetails(userId).collect{details->
                    Log.d("Coding","${details.size} updating stateflow")
                    _codingProfilesDetails.value = details
            }
        }
    }

    fun fetchSkills(userId: String){
        startOperation {
                viewProfileRepository.fetchSkills(userId).collect{listOfSkills->
                    _skills.value = listOfSkills
            }
        }
    }
}