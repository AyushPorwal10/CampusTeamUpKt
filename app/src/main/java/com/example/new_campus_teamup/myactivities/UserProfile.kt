package com.example.new_campus_teamup.myactivities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.mydataclass.EducationDetails
import com.example.new_campus_teamup.screens.profilescreens.UserProfileScreen
import com.example.new_campus_teamup.viewmodels.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfile : ComponentActivity() {

    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private var educationDetails: EducationDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userName = intent.getStringExtra("userName") ?: ""
        val userEmail = intent.getStringExtra("userEmail") ?: ""


        setContent {
            enableEdgeToEdge()
            val context = LocalContext.current



            LaunchedEffect(Unit) {
                Log.d("UserProfile", "Going to fetch user data from datastore")
                userProfileViewModel.fetchDataFromDataStore()
                userProfileViewModel.fetchEducationDetails()
                Log.d("CollegeDetails", "Fetched for first time")
            }
//            UserProfiles(userProfileViewModel)
            UserProfileScreen(userProfileViewModel, userName , userEmail)
            LaunchedEffect(Unit) {
                userProfileViewModel.errorMessage.collect { error ->
                    error?.let {
                        ToastHelper.showToast(context, error)
                        userProfileViewModel.clearError()
                    }
                }
            }
        }

    }
}