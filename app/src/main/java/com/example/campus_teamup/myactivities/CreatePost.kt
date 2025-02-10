package com.example.campus_teamup.myactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.example.campus_teamup.R
import com.example.campus_teamup.screens.CreatePostScreen

class CreatePost : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Post","MainActivity Created")
        super.onCreate(savedInstanceState)
        setContent {
            CreatePostScreen()
        }
    }
}

