package com.example.campus_teamup.myactivities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.campus_teamup.MainActivity
import com.example.campus_teamup.screens.SignUpScreen

class SignUp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignUpScreen { startActivity(Intent(this , MainActivity::class.java)) }
        }
    }
}