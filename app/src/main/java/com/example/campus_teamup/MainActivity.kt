package com.example.campus_teamup

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.campus_teamup.helper.StatusBarColor
import com.example.campus_teamup.screens.HomeScreen

import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.MyCustomTheme
import com.example.campus_teamup.ui.theme.White


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.WHITE, Color.WHITE),
            navigationBarStyle = SystemBarStyle.light(Color.BLACK, Color.BLACK)
        )

        setContent {
            MyCustomTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()  // this should be commneted since we are using top app bar
                        .background(if (isSystemInDarkTheme()) Black else White)
                ){
                    HomeScreen()
                }
                }
            }
        }
    }


