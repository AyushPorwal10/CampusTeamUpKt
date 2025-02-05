package com.example.campus_teamup.myactivities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.campus_teamup.screens.NotificationsScreen
import com.example.campus_teamup.screens.RecentChatScreen
import com.example.campus_teamup.screens.TeamDetailsScreen
import com.example.campus_teamup.screens.UserProfiles

class DrawerItemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            when (intent.getStringExtra("DrawerItem")) {
                "userProfile" -> UserProfiles()
                "notifications" -> NotificationsScreen()
                "teamDetails" -> TeamDetailsScreen()
                "recentchats" -> RecentChatScreen()
            }
        }
    }
}


