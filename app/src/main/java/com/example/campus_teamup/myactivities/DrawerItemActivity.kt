package com.example.campus_teamup.myactivities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.campus_teamup.screens.NotificationsScreen
import com.example.campus_teamup.screens.RecentChatScreen
import com.example.campus_teamup.screens.TeamDetailsScreen
import com.example.campus_teamup.viewmodels.TeamDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DrawerItemActivity : ComponentActivity() {

    private val teamDetailsViewModel : TeamDetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            when (intent.getStringExtra("DrawerItem")) {
                "notifications" -> NotificationsScreen()
                "teamDetails" -> TeamDetailsScreen()
                "recentchats" -> RecentChatScreen()
            }
        }

        teamDetailsViewModel.initializeUserId()
    }
}


