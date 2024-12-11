package com.example.campus_teamup.helper

import android.app.Activity
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.White

@Composable
fun StatusBarColor(
    theme  : Boolean

) {
    val barColor = if(theme) Black.toArgb() else White.toArgb()
    val activity = LocalContext.current as ComponentActivity

    LaunchedEffect(theme){
        if(theme){
            activity.enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(
                    barColor , barColor
                )
            )
        }
        else {
            activity.enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(
                    barColor ,
                ),
                navigationBarStyle = SystemBarStyle.dark(
                    barColor,
                )
            )
        }

    }
}

