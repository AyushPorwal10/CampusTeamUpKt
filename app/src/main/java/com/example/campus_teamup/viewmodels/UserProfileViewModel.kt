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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
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
    private lateinit var phoneNumber : String
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val _currentUserImage = MutableStateFlow("")
    val currentUserImage: StateFlow<String> = _currentUserImage.asStateFlow()

    private val _collegeDetails = MutableStateFlow<CollegeDetails?>(null)
    val collegeDetails: StateFlow<CollegeDetails?> = _collegeDetails

    private val _codingProfiles = MutableStateFlow<List<String>>(emptyList())
    val codingProfiles : StateFlow<List<String>> get() = _codingProfiles.asStateFlow()


    init {
        viewModelScope.launch {
            userManager.userData
                .filter { it.userId.isNotEmpty() && it.phoneNumber.isNotEmpty() }
                .first()
                .let { userData ->
                    userId = userData.userId
                    userName = userData.userName
                    phoneNumber = userData.phoneNumber
                    collegeName = userData.collegeName


                    Log.d("Learning", "UserId: ${userData.userId}")
                    observeCurrentUserImage()
                    fetchCodingProfiles()
                }
        }

        viewModelScope.launch {
            userManager.userData.collectLatest {
                userId = it.userId
                userName = it.userName
                phoneNumber = it.phoneNumber
                Log.d("Learning","Initialized data user id is $userId username is $userName phonenumber is $phoneNumber")
            }
        }
    }

    suspend fun fetchDataFromDataStore() {
        Log.d("CollegeDetails", "Fetching of data from datastore started")
        val userData = userManager.userData.first()
        userId = userData.userId
        collegeName = userData.collegeName
        userName = userData.userName
        phoneNumber = userData.phoneNumber
        Log.d("UserProfile", "Updated User Id: $userId")
        Log.d("UserProfile", "Fetched User Id $collegeName")
        Log.d("UserProfile", "Fetched phoneNumber is  $phoneNumber")
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
                userProfileRepo.saveCollegeDetails(userId , phoneNumber, newCollegeDetails)
            }

            _collegeDetails.value = newCollegeDetails
            _isLoading.value = false

        }
    }

    fun observeCurrentUserImage(){
        Log.d("CollegeDetails","User id in observing image is $userId")
        viewModelScope.launch {

            userProfileRepo.observeCurrentUserImage(userId).catch {

            }.collect{
                _currentUserImage.value = it
            }
        }

    }

    fun fetchCollegeDetails() {

        viewModelScope.launch {
            _isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                Log.d("CollegeDetails", "$userId data fetching process started")
                userProfileRepo.fetchCollegeDetails(userId , phoneNumber).toObject(CollegeDetails::class.java)
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
                userProfileRepo.saveCodingProfiles(phoneNumber, listOfCodingProfiles)
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

    private suspend fun fetchCodingProfiles() {
        _isLoading.value = true

        if(phoneNumber.isNotEmpty()){
            Log.d("Learning","Phone number inside coding profiles is $phoneNumber")
            userProfileRepo.fetchCodingProfiles(phoneNumber).collect {
                Log.d("Learning","Fetching Coding Profiles")
                _isLoading.value = false
                _codingProfiles.value = it
            }
        }
        else{
            Log.d("Learning","Phone number is emptyr")
        }

    }

    suspend fun saveSkills(listOfSkills: List<String>) {
        userProfileRepo.saveSkills(phoneNumber, listOfSkills)
    }

    suspend fun fetchSkills(): List<String> {
        val documentSnapshot = userProfileRepo.fetchSkills(phoneNumber)
        return documentSnapshot.get("skillList") as? List<String> ?: emptyList()
    }
}