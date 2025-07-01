package com.example.new_campus_teamup.myactivities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.mydataclass.CollegeDetails
import com.example.new_campus_teamup.userprofile.screens.UserProfiles
import com.example.new_campus_teamup.viewmodels.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfile : ComponentActivity() {

    private val userProfileViewModel : UserProfileViewModel by viewModels()
     private var collegeDetails: CollegeDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            val context = LocalContext.current



                LaunchedEffect(Unit) {
                    Log.d("UserProfile", "Going to fetch user data from datastore")
                    userProfileViewModel.fetchDataFromDataStore()

                    userProfileViewModel.fetchCollegeDetails()
                    Log.d("CollegeDetails", "Fetched for first time")
                }
            UserProfiles(userProfileViewModel)

            LaunchedEffect(Unit){
                userProfileViewModel.errorMessage.collect{error->
                    error?.let {
                        ToastHelper.showToast(context ,error)
                        userProfileViewModel.clearError()
                    }
                }
            }
        }

    }
}