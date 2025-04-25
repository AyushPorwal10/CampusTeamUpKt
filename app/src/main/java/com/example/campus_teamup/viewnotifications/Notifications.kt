package com.example.campus_teamup.viewnotifications

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.LoadAnimation
import com.example.campus_teamup.helper.rememberNetworkStatus
import com.example.campus_teamup.myactivities.UserData
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.ViewNotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(viewNotificationViewModel: ViewNotificationViewModel, currentUserData : UserData?) {
    val bgColor = BackGroundColor
    val textColor = White

    val activity = LocalContext.current as? Activity
    val isConnected = rememberNetworkStatus()
    val snackbarHostState = remember { SnackbarHostState() }

    Log.d("UserData","Notification Screen UserId from datastore is ${currentUserData?.userId} ")

    val combinedNotificationList = viewNotificationViewModel.combineNotificationList.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.notifications)) },
                colors = topAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        activity?.finish()
                    }) {
                        Icon(painter = painterResource(id = R.drawable.browseback), contentDescription =null , tint = textColor )
                    }
                },

            )
        }
     , content= {paddingValues ->

            LaunchedEffect(isConnected) {
                if (!isConnected) {
                    snackbarHostState.showSnackbar(
                        message = "No Internet Connection",
                        actionLabel = "OK"
                    )
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor)
                    .padding(paddingValues)
            ) {
                if (combinedNotificationList.value.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadAnimation(
                            modifier = Modifier.size(200.dp),
                            animation = R.raw.nonotification,
                            playAnimation = true
                        )
                    }
                } else if (isConnected) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(combinedNotificationList.value) { index, item ->
                            when (item) {
                                is NotificationItems.TeamInviteNotification -> {
                                    Log.d("ShowNotification", "Showing TeamInviteNotification")
                                    TeamInviteNotification(item, index, viewNotificationViewModel, currentUserData)
                                }

                                is NotificationItems.MemberInviteNotification -> {
                                    Log.d("ShowNotification", "Showing MemberInviteNotification")
                                    TeamJointNotification(item, index, viewNotificationViewModel, currentUserData)
                                }
                            }
                        }
                    }
                }
                else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadAnimation(
                            modifier = Modifier.size(200.dp),
                            animation = R.raw.nonetwork,
                            playAnimation = true
                        )
                    }
                }
            }
        })
}