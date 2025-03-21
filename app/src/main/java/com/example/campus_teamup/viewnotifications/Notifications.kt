package com.example.campus_teamup.viewnotifications

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.R
import com.example.campus_teamup.myactivities.UserData
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.ViewNotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(viewNotificationViewModel: ViewNotificationViewModel, currentUserData : UserData?) {
    val bgColor = BackGroundColor
    val textColor = White


    Log.d("UserData","Notification Screen UserId from datastore is ${currentUserData?.userId} ")

    val listOfNotification = viewNotificationViewModel.teamInviteList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = topAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                ),
                navigationIcon = {
                    IconButton(onClick = {

                    }) {
                        Icon(painter = painterResource(id = R.drawable.browseback), contentDescription =null , tint = textColor )
                    }
                },

            )
        }
     , content= {paddingValues ->
         LazyColumn(
             modifier = Modifier
                 .padding(paddingValues)
                 .fillMaxSize()
                 .background(bgColor)
                 , 
             verticalArrangement = Arrangement.spacedBy(8.dp),
             horizontalAlignment = Alignment.CenterHorizontally,
         ){
             itemsIndexed(listOfNotification.value) { index, item ->
                 TeamInviteNotification(item, index , viewNotificationViewModel , currentUserData)
             }
         }
        })
}