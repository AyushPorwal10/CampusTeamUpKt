package com.example.campus_teamup.myactivities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import com.example.campus_teamup.MainActivity
import com.example.campus_teamup.helper.StatusBarColor
import com.example.campus_teamup.myThemes.PrimaryBlack
import com.example.campus_teamup.myThemes.PrimaryWhiteGradient
import com.example.campus_teamup.screens.SignUpScreen

class SignUp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.WHITE, Color.WHITE),
            navigationBarStyle = SystemBarStyle.light(Color.BLACK, Color.BLACK)
        )
        setContent {

            Box(modifier = Modifier.background(if(isSystemInDarkTheme()) PrimaryBlack else PrimaryWhiteGradient) ){

            }

            SignUpScreen { startActivity(Intent(this , MainActivity::class.java)) }
        }
    }
}