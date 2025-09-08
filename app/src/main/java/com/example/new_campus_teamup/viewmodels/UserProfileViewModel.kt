package com.example.new_campus_teamup.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.new_campus_teamup.UiState
import com.example.new_campus_teamup.helper.CheckNetworkConnectivity
import com.example.new_campus_teamup.myactivities.UserManager
import com.example.new_campus_teamup.mydataclass.EducationDetails
import com.example.new_campus_teamup.myrepository.UserProfileRepo
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
    private val networkMonitor: CheckNetworkConnectivity,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private lateinit var userId: String
    private lateinit var userEmail: String
    private lateinit var collegeName: String
    private lateinit var userName: String
    private lateinit var phoneNumber: String
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _skills = MutableStateFlow<List<String>>(emptyList())
    val skills: StateFlow<List<String>> = _skills

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _currentUserImage = MutableStateFlow("")
    val currentUserImage: StateFlow<String> = _currentUserImage.asStateFlow()

    private val _educationDetails = MutableStateFlow<EducationDetails?>(null)
    val educationDetails: StateFlow<EducationDetails?> = _educationDetails

    private val _codingProfiles = MutableStateFlow<List<String>>(emptyList())
    val codingProfiles: StateFlow<List<String>> get() = _codingProfiles.asStateFlow()

    private val _deleteUserAccountUiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteUserAccountUiState: StateFlow<UiState<Unit>> get() = _deleteUserAccountUiState.asStateFlow()


    init {
        startOperation {
            userManager.userData
                .filter {
                    it.userId.isNotEmpty()
                            && it.userName.isNotEmpty()
                            && it.email.isNotEmpty()
                            && it.collegeName.isNotEmpty()
                }
                .first()
                .let { userData ->
                    userId = userData.userId
                    userName = userData.userName
                    phoneNumber = userData.phoneNumber
                    collegeName = userData.collegeName
                    userEmail = userData.email
                    fetchSkills()
                    Log.d("Learning", "UserId: ${userData.userId}")
                    observeCurrentUserImage()
                    fetchCodingProfiles()
                }
        }

        startOperation {
            userManager.userData.collectLatest {
                userId = it.userId
                userName = it.userName
                phoneNumber = it.phoneNumber
                Log.d(
                    "Learning",
                    "Initialized data user id is $userId username is $userName phonenumber is $phoneNumber"
                )
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }


    private fun startOperation(block: suspend () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true;
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
            } finally {
                _isLoading.value = false
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
        userEmail = userData.email
        Log.d("UserProfile", "Updated User Id: $userId")
        Log.d("UserProfile", "Fetched User Id $collegeName")
        Log.d("UserProfile", "Fetched phoneNumber is  $phoneNumber")
    }


    // college details
    fun saveEducationDetails(
        year: String,
        branch: String,
        course: String,
        onSuccess: () -> Unit
    ) {

        startOperation {
            Log.d("CollegeDetails", "Going to save collegeDetails")

//            _isLoading.value = true


            val newEducationDetails = EducationDetails(
                userName,
                collegeName,
                year,
                course,
                branch
            )

            userProfileRepo.saveEducationDetails(userId, newEducationDetails)

            _educationDetails.value = newEducationDetails
            onSuccess()
            // _isLoading.value = false

        }
    }

    private fun observeCurrentUserImage() {
        Log.d("CollegeDetails", "User id in observing image is $userId")
        startOperation {
            userProfileRepo.observeCurrentUserImage(userId).catch {

            }.collect {
                _currentUserImage.value = it ?: ""
            }
        }
    }

    fun fetchEducationDetails() {

        startOperation {
            val result = withContext(Dispatchers.IO) {
                Log.d("CollegeDetails", "$userId data fetching process started")
                userProfileRepo.fetchEducationDetails(userId).toObject(EducationDetails::class.java)
            }
            _educationDetails.value = result
        }
    }

    // coding profiles

    fun saveCodingProfiles(
        listOfCodingProfiles: List<String>,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        startOperation {
            try {
                Log.d("CodingProfiles", "$userId saving profiles")
                userProfileRepo.saveCodingProfiles(userId, listOfCodingProfiles)  // suspend call
                onSuccess() // called only after suspend function completes successfully
            } catch (e: Exception) {
                Log.e("CodingProfiles", "Failed to save profiles: ${e.message}")
                onError()
            }


        }


    }

    fun saveImageUrl(userImageUri : Uri, onSuccess: () -> Unit){
        startOperation {
            userProfileRepo.saveUserImage(userId, userImageUri , onSuccess = {
                onSuccess()
            })
        }
    }
    private fun fetchCodingProfiles() {
        //_isLoading.value = true
        startOperation {
            if (userId.isNotEmpty()) {
                Log.d("Learning", "User id inside coding profiles is $userId")
                userProfileRepo.fetchCodingProfiles(userId).collect {
                    Log.d("Learning", "Fetching Coding Profiles")
                    //_isLoading.value = false
                    _codingProfiles.value = it
                    Log.d("Learning", "Fetched coding profiles size is ${it.size}")
                }
            } else {
                Log.d("Learning", "User id is empty")
            }
        }
    }
    fun delete(){
        viewModelScope.launch {
            userProfileRepo.deleteAllData()
        }
    }

    fun saveSkills(listOfSkills: List<String>, onSuccess: () -> Unit) {
        startOperation {
            userProfileRepo.saveSkills(userId, listOfSkills)
            _skills.value = listOfSkills // this is to update prev skills with current skills
            onSuccess()
        }
    }

    fun fetchSkills() {
        startOperation {
            val documentSnapshot = userProfileRepo.fetchSkills(userId)
            val list = documentSnapshot.get("skillList") as? List<String> ?: emptyList()
            Log.d("Learning", "Fetched skills size is ${list.size}")
            _skills.value = list
        }
    }

    fun deleteUserAccount(){
        viewModelScope.launch {
            Log.d("Firebase_Function_Testing","User email is $userEmail")

            userProfileRepo.deleteUserAccount(userEmail, onStateChange = {
                _deleteUserAccountUiState.value = it
            })
        }
    }

    fun logoutUser(onLogoutSuccess: () -> Unit) {
        startOperation {
            userManager.clearUserData()
            firebaseAuth.signOut()
            onLogoutSuccess()
        }
    }


    fun clearDeleteAccountUiState(){
        _deleteUserAccountUiState.value = UiState.Idle
    }


}