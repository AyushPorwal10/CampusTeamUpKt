package com.example.campus_teamup.myactivities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.campus_teamup.chatsection.screens.ChatScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Chat : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
        }
    }
}