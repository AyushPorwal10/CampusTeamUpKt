package com.example.new_campus_teamup.myactivities

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.roleprofile.screens.ViewUserProfiles
import com.example.new_campus_teamup.viewmodels.NotificationViewModel
import com.example.new_campus_teamup.viewmodels.ViewProfileViewModel
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
            val context = LocalContext.current

            ViewUserProfiles(viewProfileViewModel ,notificationViewModel ,  userId , receiverPhoneNumber)


            LaunchedEffect(Unit){
                notificationViewModel.errorMessage.collect{error->
                    error?.let {
                        ToastHelper.showToast(context ,error)
                        notificationViewModel.clearError()
                    }
                }
            }

            LaunchedEffect(Unit){
                viewProfileViewModel.errorMessage.collect{error->
                    error?.let {
                        ToastHelper.showToast(context ,error)
                        notificationViewModel.clearError()
                    }
                }
            }

        }
        Log.d("ViewUserProfile","Going to fetch college details")

        // this will check if already request has been sent or not
        notificationViewModel.fetchSenderId()

        notificationViewModel.checkIfAlreadyRequestSent(userId!!)
        viewProfileViewModel.fetchCollegeDetails(userId )
        viewProfileViewModel.fetchCodingProfileDetails(userId  )
        viewProfileViewModel.fetchSkills(userId)
    }
}