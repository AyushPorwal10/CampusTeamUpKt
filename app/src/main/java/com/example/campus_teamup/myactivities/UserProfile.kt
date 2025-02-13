package com.example.campus_teamup.myactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.example.campus_teamup.R
import com.example.campus_teamup.screens.UserProfiles
import com.example.campus_teamup.viewmodels.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfile : ComponentActivity() {
    private val userProfileViewModel : UserProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent{

            LaunchedEffect(Unit){
                Log.d("UserProfile","Going to fetch user data from datastore")
                userProfileViewModel.fetchDataFromDataStore()
            }

            UserProfiles(userProfileViewModel)
        }
    }
}