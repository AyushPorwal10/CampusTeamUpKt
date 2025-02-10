package com.example.campus_teamup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.mydataclass.CollegeDetails
import com.example.campus_teamup.myrepository.UserProfileRepo
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserProfileViewModel @Inject constructor(
private val userProfileRepo: UserProfileRepo,
    private val userManager: UserManager
) : ViewModel(){

    private lateinit var userId : String
    private lateinit var collegeName : String

    suspend fun getUserIdFromDataStore(){
        userManager.userData.collect { data ->
            userId = data.userId
            collegeName = data.collegeName
        }
        Log.d("CollegeDetails", "Updated User Id: $userId")
        Log.d("CollegeDetails","Fetched User Id $collegeName")
    }
    // college details
    suspend fun saveCollegeDetails( year : String , branch : String , course : String){
        viewModelScope.launch {
            Log.d("CollegDetails" , "Going to save collegeDetails")
            userProfileRepo.saveCollegeDetails(userId,
                CollegeDetails(
                year,
                course,
                branch
            ))
        }
    }
    suspend fun fetchCollegeDetails() : CollegeDetails? {
        Log.d("CollegeDetails","$userId data fetching process started")
       return userProfileRepo.fetchCollegeDetails(userId).toObject(CollegeDetails::class.java)
    }

    // coding profiles

    suspend fun saveCodingProfiles(listOfCodingProfiles : List<String>){
        Log.d("CodingProfiles","$userId saving profiles")
        userProfileRepo.saveCodingProfiles(userId , listOfCodingProfiles)
    }

}