package com.example.campus_teamup.myactivities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.campus_teamup.roleprofile.screens.ViewUserProfiles
import com.example.campus_teamup.viewmodels.ViewProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewUserProfile : ComponentActivity() {
    private val viewProfileViewModel : ViewProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = intent.getStringExtra("userId")

        Log.d("ViewUserProfile","Received UserId $userId")
        setContent{
            ViewUserProfiles(viewProfileViewModel , userId)
        }
        Log.d("ViewUserProfile","Going to fetch college details")

        viewProfileViewModel.fetchCollegeDetails(userId!!)
        viewProfileViewModel.fetchCodingProfileDetails(userId)
        viewProfileViewModel.fetchSkills(userId)
    }
    override fun onPause() {
        super.onPause()
        Log.d("Activity","ViewUserProfile is on Pause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Activity","ViewUserProfile is on Resume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Activity","ViewUserProfile is Destroyed")

    }
}