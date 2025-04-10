package com.example.campus_teamup.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.mydataclass.CollegeDetails
import com.example.campus_teamup.myrepository.UserProfileRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userProfileRepo: UserProfileRepo,
    private val userManager: UserManager,
) : ViewModel() {

    private lateinit var userId: String
    private lateinit var collegeName: String
    private lateinit var userName: String
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _collegeDetails = MutableStateFlow<CollegeDetails?>(null)
    val collegeDetails: StateFlow<CollegeDetails?> = _collegeDetails

    private val _codingProfiles = MutableStateFlow<List<String>>(emptyList())
    val codingProfiles : StateFlow<List<String>> get() = _codingProfiles.asStateFlow()


    init {
        viewModelScope.launch {
            userManager.userData
                .filter { it.userId.isNotEmpty() }
                .first()
                .let { userData ->
                    Log.d("Learning", "UserId: ${userData.userId}")
                    fetchCodingProfiles(userData.userId)
                }
        }

    }

    suspend fun fetchDataFromDataStore() {
        Log.d("CollegeDetails", "Fetching of data from datastore started")
        val userData = userManager.userData.first()
        userId = userData.userId
        collegeName = userData.collegeName
        userName = userData.userName

        Log.d("UserProfile", "Updated User Id: $userId")
        Log.d("UserProfile", "Fetched User Id $collegeName")
    }



    // college details
    fun saveCollegeDetails(
        userImageUrl: String,
        year: String,
        branch: String,
        course: String
    ) {
        Log.d("CollegeDetails", userImageUrl)

        viewModelScope.launch {
            Log.d("CollegeDetails", "Going to save collegeDetails")

            _isLoading.value = true


            val newCollegeDetails = CollegeDetails(
                userName,
                collegeName,
                year,
                course,
                branch,
                userImageUrl
            )

            withContext(Dispatchers.IO) {
                userProfileRepo.saveCollegeDetails(userId, newCollegeDetails)
            }

            _collegeDetails.value = newCollegeDetails
            _isLoading.value = false

        }
    }

    fun fetchCollegeDetails() {

        viewModelScope.launch {
            _isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                Log.d("CollegeDetails", "$userId data fetching process started")
                userProfileRepo.fetchCollegeDetails(userId).toObject(CollegeDetails::class.java)
            }
            _isLoading.value = false
            Log.d("Image","Fetched Image in viewmodel url is ${result?.userImageUrl}")
            _collegeDetails.value = result
        }
    }

    // coding profiles

    fun saveCodingProfiles(listOfCodingProfiles: List<String>) {
        viewModelScope.launch {
            Log.d("CodingProfiles", "$userId saving profiles")
            _isLoading.value = true

            withContext(Dispatchers.IO) {
                userProfileRepo.saveCodingProfiles(userId, listOfCodingProfiles)
            }
            _isLoading.value = false
        }


    }

    fun uploadUserImageToStorage(imageUri: Uri, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                userProfileRepo.uploadUserImageToStorage(userId, imageUri)
            }
            _isLoading.value = false
            onResult(result)

        }

    }

    private suspend fun fetchCodingProfiles(currentUserId: String) {
        _isLoading.value = true
         userProfileRepo.fetchCodingProfiles(currentUserId).collect {
             Log.d("Learning","Fetching Coding Profiles")
             _isLoading.value = false
             _codingProfiles.value = it
         }
    }

    suspend fun saveSkills(listOfSkills: List<String>) {
        userProfileRepo.saveSkills(userId, listOfSkills)
    }

    suspend fun fetchSkills(): List<String> {
        val documentSnapshot = userProfileRepo.fetchSkills(userId)
        return documentSnapshot.get("skillList") as? List<String> ?: emptyList()
    }
}