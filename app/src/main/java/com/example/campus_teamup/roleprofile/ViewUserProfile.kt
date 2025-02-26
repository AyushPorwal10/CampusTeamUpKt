package com.example.campus_teamup.roleprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.campus_teamup.R
import com.example.campus_teamup.roleprofile.screens.ViewUserProfiles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewUserProfile : ComponentActivity() {
    private val viewProfileViewModel : ViewProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = intent.getStringExtra("userId")
        Log.d("ViewUserProfile","Received UserId $userId")
        setContent{
            ViewUserProfiles(viewProfileViewModel)
        }
        Log.d("ViewUserProfile","Going to fetch college details")

        viewProfileViewModel.fetchCollegeDetails(userId!!)
        viewProfileViewModel.fetchCodingProfileDetails(userId)
        viewProfileViewModel.fetchSkills(userId)
    }
}