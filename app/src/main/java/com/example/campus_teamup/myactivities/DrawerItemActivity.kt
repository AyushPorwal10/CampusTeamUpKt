package com.example.campus_teamup.myactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.campus_teamup.R
import com.example.campus_teamup.screens.NotificationsScreen
import com.example.campus_teamup.screens.TeamDetailScreen

class DrawerItemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val screenToOpen = intent.getStringExtra("DrawerItem")

            if(screenToOpen.equals("notifications")){
                NotificationsScreen()
            }
            else if(screenToOpen.equals("teamDetails")){
                TeamDetailScreen()
            }


        }
    }
}