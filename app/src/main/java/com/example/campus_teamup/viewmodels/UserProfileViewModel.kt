package com.example.campus_teamup.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.mydataclass.CollegeDetails
import com.example.campus_teamup.myrepository.UserProfileRepo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userProfileRepo: UserProfileRepo,
    private val userManager: UserManager,
    private val storageReference: StorageReference
) : ViewModel() {

    private lateinit var userId: String
    private lateinit var collegeName: String
    private lateinit var userName: String

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading : StateFlow<Boolean> = _isLoading

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
        viewModelScope.launch {
            Log.d("CollegDetails", "Going to save collegeDetails")

            _isLoading.value = true

            withContext(Dispatchers.IO){
                userProfileRepo.saveCollegeDetails(
                    userId,
                    CollegeDetails(
                        userName,
                        collegeName,
                        year,
                        course,
                        branch,
                        userImageUrl
                    )
                )
            }
            _isLoading.value = false

        }
    }

     fun fetchCollegeDetails(onResult: (CollegeDetails?) -> Unit) {

        viewModelScope.launch {
            _isLoading.value = true
            val result = withContext(Dispatchers.IO){
                Log.d("CollegeDetails", "$userId data fetching process started")
                userProfileRepo.fetchCollegeDetails(userId).toObject(CollegeDetails::class.java)
            }
            _isLoading.value = false
            onResult(result)
        }
    }

    // coding profiles

     fun saveCodingProfiles(listOfCodingProfiles: List<String>) {
        viewModelScope.launch {
            Log.d("CodingProfiles", "$userId saving profiles")
            _isLoading.value = true

            withContext(Dispatchers.IO){
                userProfileRepo.saveCodingProfiles(userId, listOfCodingProfiles)
            }
            _isLoading.value = false
        }


    }

     fun uploadUserImageToStorage(imageUri: Uri , onResult : (String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = withContext(Dispatchers.IO){
                  userProfileRepo.uploadUserImageToStorage(userId, imageUri)
            }
            _isLoading.value = false
            onResult(result)

        }

    }

    suspend fun fetchCodingProfiles(onResult: (List<String>) -> Unit){
        viewModelScope.launch {
            _isLoading.value = true

            Log.d("CodingProfiles","Going to fetch coding profiles ")
            val documentSnapshot = withContext(Dispatchers.IO){
                userProfileRepo.fetchCodingProfiles(userId)
            }
            _isLoading.value = false
            val result = documentSnapshot.get("profilelist") as? List<String> ?: emptyList()
            onResult(result)
        }

    }

    suspend fun saveSkills(listOfSkills : List<String>){
        userProfileRepo.saveSkills(userId , listOfSkills)
    }
    suspend fun fetchSkills() : List<String>{
        val documentSnapshot =  userProfileRepo.fetchSkills(userId)
        return documentSnapshot.get("skillList") as? List<String> ?: emptyList()
    }
}