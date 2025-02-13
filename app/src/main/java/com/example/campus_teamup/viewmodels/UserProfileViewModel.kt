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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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
    suspend fun saveCollegeDetails(
        userImageUrl: String,
        year: String,
        branch: String,
        course: String
    ) {
        viewModelScope.launch {
            Log.d("CollegDetails", "Going to save collegeDetails")
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
    }

    suspend fun fetchCollegeDetails(): CollegeDetails? {
        Log.d("CollegeDetails", "$userId data fetching process started")
        return userProfileRepo.fetchCollegeDetails(userId).toObject(CollegeDetails::class.java)
    }

    // coding profiles

    suspend fun saveCodingProfiles(listOfCodingProfiles: List<String>) {
        Log.d("CodingProfiles", "$userId saving profiles")
        userProfileRepo.saveCodingProfiles(userId, listOfCodingProfiles)
    }

    suspend fun uploadUserImageToStorage(imageUri: Uri): String? {
        return userProfileRepo.uploadUserImageToStorage(userId, imageUri)
    }

    suspend fun fetchCodingProfiles() : List<String>{
        Log.d("CodingProfiles","Going to fetch coding profiles ")
        val documentSnapshot = userProfileRepo.fetchCodingProfiles(userId)
        return documentSnapshot.get("profilelist") as? List<String> ?: emptyList()
    }

    suspend fun saveSkills(listOfSkills : List<String>){
        userProfileRepo.saveSkills(userId , listOfSkills)
    }
    suspend fun fetchSkills() : List<String>{
        val documentSnapshot =  userProfileRepo.fetchSkills(userId)
        return documentSnapshot.get("skillList") as? List<String> ?: emptyList()
    }
}