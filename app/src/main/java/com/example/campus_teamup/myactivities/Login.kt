package com.example.campus_teamup.myactivities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import com.example.campus_teamup.screens.LoginRedesign
import com.example.campus_teamup.MainActivity
import com.example.campus_teamup.ui.theme.BackGroundColor

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(modifier = Modifier.background(BackGroundColor) ){

            }
            LoginRedesign ( {startActivity(Intent(this , SignUp::class.java))} , { startActivity(Intent(this, MainActivity::class.java)) })
        }
    }
}