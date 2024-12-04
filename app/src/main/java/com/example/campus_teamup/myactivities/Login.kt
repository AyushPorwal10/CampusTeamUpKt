package com.example.campus_teamup.myactivities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import com.example.campus_teamup.MainActivity
import com.example.campus_teamup.myThemes.PrimaryBlack
import com.example.campus_teamup.myThemes.PrimaryWhiteGradient
import com.example.campus_teamup.screens.LoginScreen

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(modifier = Modifier.background(if(isSystemInDarkTheme()) PrimaryBlack else PrimaryWhiteGradient) ){

            }
            LoginScreen ( {startActivity(Intent(this , SignUp::class.java))} , { startActivity(Intent(this, MainActivity::class.java)) })
        }
    }
}