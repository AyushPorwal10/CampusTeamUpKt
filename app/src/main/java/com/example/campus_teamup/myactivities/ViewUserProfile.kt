package com.example.campus_teamup.myactivities

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.campus_teamup.roleprofile.screens.ViewUserProfiles
import com.example.campus_teamup.viewmodels.NotificationViewModel
import com.example.campus_teamup.viewmodels.ViewProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewUserProfile : ComponentActivity() {
    private val viewProfileViewModel : ViewProfileViewModel by viewModels()
    private val notificationViewModel : NotificationViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // this is receiver
        val userId = intent.getStringExtra("userId")
        val receiverPhoneNumber = intent.getStringExtra("phone_number") // person who posted this role

        Log.d("FCM","Received UserId $userId <-")
        setContent{
            ViewUserProfiles(viewProfileViewModel ,notificationViewModel ,  userId , receiverPhoneNumber)
        }
        Log.d("ViewUserProfile","Going to fetch college details")

        // this will check if already request has been sent or not
        notificationViewModel.fetchSenderId()

        notificationViewModel.checkIfAlreadyRequestSent(userId!!)
        viewProfileViewModel.fetchCollegeDetails(userId , receiverPhoneNumber)
        viewProfileViewModel.fetchCodingProfileDetails(userId , receiverPhoneNumber )
        viewProfileViewModel.fetchSkills(userId, receiverPhoneNumber)
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