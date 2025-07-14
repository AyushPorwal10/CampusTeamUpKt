package com.example.new_campus_teamup.myactivities

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.new_campus_teamup.chatsection.screens.ChatScreen
import com.example.new_campus_teamup.helper.StartChatDialog
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.saveditems.ShowSavedItems
import com.example.new_campus_teamup.screens.FeedbackScreen
import com.example.new_campus_teamup.viewnotifications.NotificationsScreen
import com.example.new_campus_teamup.screens.RecentChatScreen
import com.example.new_campus_teamup.viewmodels.SavedItemsViewModel
import com.example.new_campus_teamup.viewmodels.UserDataSharedViewModel
import com.example.new_campus_teamup.viewmodels.ViewNotificationViewModel
import com.example.new_campus_teamup.yourposts.YourPost
import com.example.new_campus_teamup.yourposts.YourPostViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DrawerItemActivity : ComponentActivity() {

    private val viewNotificationViewModel: ViewNotificationViewModel by viewModels()
    private val savedItemsViewModel : SavedItemsViewModel by viewModels()
    private val userDataSharedViewModel : UserDataSharedViewModel by viewModels()
    private val yourPostViewModel : YourPostViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()

            val currentUserData = userDataSharedViewModel.userData.collectAsState()

            Log.d("UserData","In Drawer Activity UserId from datastore is ${currentUserData.value?.userId} ")

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
                "recentchats" -> "recentchats"
                "savedItems" -> "savedItems"
                "feedback" -> "feedback"
                "yourposts" -> "yourposts"
                else -> "notifications"
            }

            NavHost(navController, startDestination = startDestination) {
                composable("notifications") {
                    viewNotificationViewModel.fetchCombinedNotifications(currentUserData.value?.userId)
                    NotificationsScreen(viewNotificationViewModel ,currentUserData.value)
                }
                composable("recentchats") {
                    RecentChatScreen(startChat = {data->
                        val senderName = data.senderName
                        val chatRoomId = data.chatRoomId

                        navController.navigate("chatScreen/$senderName/$chatRoomId/${currentUserData.value?.userId}")
                    })
                }
                composable("chatScreen/{senderName}/{chatRoomId}/{currentUserId}"){backStackEntry->
                    val senderName = backStackEntry.arguments?.getString("senderName")
                    val chatRoomId = backStackEntry.arguments?.getString("chatRoomId")
                    val currentUserId = backStackEntry.arguments?.getString("currentUserId")
                    ChatScreen(senderName,chatRoomId , currentUserId)
                }
                composable("yourposts"){
                    yourPostViewModel.fetchUserPostedRoles()
                    yourPostViewModel.fetchUserPostedVacancy()
                    yourPostViewModel.fetchUserPostedProjects()
                    YourPost(yourPostViewModel)
                }


                composable("savedItems"){
                    savedItemsViewModel.fetchSavedProjects(currentUserData.value?.userId)
                    savedItemsViewModel.fetchSavedRoles(currentUserData.value?.userId)
                    savedItemsViewModel.fetchSavedVacancy(currentUserData.value?.userId)
                    ShowSavedItems(currentUserData.value , savedItemsViewModel)
                }

                composable ("feedback"){
                    FeedbackScreen(onSubmit = {
                        userDataSharedViewModel.sendFeedback(it , onError = {
                            ToastHelper.showToast(this@DrawerItemActivity , "Something Went Wrong !")
                        })
                    } , userDataSharedViewModel)
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

    override fun onDestroy() {
        super.onDestroy()
        Log.d("NavigationTesting","Drawer destroyed")
    }
}