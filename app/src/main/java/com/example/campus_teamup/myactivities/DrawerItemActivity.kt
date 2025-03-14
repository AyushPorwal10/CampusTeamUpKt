package com.example.campus_teamup.myactivities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campus_teamup.helper.StartChatDialog
import com.example.campus_teamup.viewnotifications.NotificationsScreen
import com.example.campus_teamup.screens.RecentChatScreen
import com.example.campus_teamup.screens.TeamDetailsScreen
import com.example.campus_teamup.viewmodels.TeamDetailsViewModel
import com.example.campus_teamup.viewmodels.ViewNotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope


@AndroidEntryPoint
class DrawerItemActivity : ComponentActivity() {

    private val teamDetailsViewModel: TeamDetailsViewModel by viewModels()
    private val viewNotificationViewModel: ViewNotificationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewNotificationViewModel.fetchUserDataFromDatastore()
            var showDialog by remember{
                mutableStateOf(false)
            }
            var navigateToChatScreen by remember{
                mutableStateOf(false)
            }
            val navController = rememberNavController()
            val isChatRoomCreated by viewNotificationViewModel.isChatRoomCreated.collectAsState()
            val startDestination = when (intent.getStringExtra("DrawerItem")) {
                "notifications" -> "notifications"
                "teamDetails" -> "teamDetails"
                "recentchats" -> "recentschats"
                else -> "notifications"
            }

            NavHost(navController, startDestination = startDestination) {
                composable("notifications") {
                    viewNotificationViewModel.fetchTeamInviteNotifications()
                    NotificationsScreen(viewNotificationViewModel)
                }
                composable("teamDetails") {
                    teamDetailsViewModel.initializeUserId()
                    TeamDetailsScreen()
                }
                composable("recentchats") {
                    RecentChatScreen()
                }
            }

            LaunchedEffect(isChatRoomCreated){
                if(isChatRoomCreated)
                    showDialog = true
            }

            LaunchedEffect(navigateToChatScreen){
                if(navigateToChatScreen){
                    navController.navigate("recentchats")
                    navigateToChatScreen = false
                }
            }


            if(showDialog){
                StartChatDialog {
                    showDialog = false
                    navigateToChatScreen = true
                }
            }




        }
    }
}