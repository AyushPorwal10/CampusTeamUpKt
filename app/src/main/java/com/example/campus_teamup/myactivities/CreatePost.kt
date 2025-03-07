package com.example.campus_teamup.myactivities

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import com.example.campus_teamup.roleprofile.screens.CreatePostScreen
import com.example.campus_teamup.viewmodels.CreatePostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePost : ComponentActivity() {
    private val createPostViewModel : CreatePostViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Post","Activity Created")
        super.onCreate(savedInstanceState)
        setContent {
            val screenToOpen = intent.getStringExtra("status")

            LaunchedEffect(Unit){
                createPostViewModel.fetchDataFromDataStore()
            }
            CreatePostScreen(screenToOpen!! , createPostViewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PostRole","Activity Destroyed")
    }

    override fun onResume() {
        super.onResume()
        Log.d("PostRole","Activity Resumed")
    }

    override fun onPause() {
        super.onPause()
        Log.d("PostRole","Activity Paused")
    }
}

