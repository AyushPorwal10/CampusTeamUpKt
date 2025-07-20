package com.example.new_campus_teamup.roleprofile.screens

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.ShowRequestDialog
import com.example.new_campus_teamup.myAnimation.FloatingBubbles
import com.example.new_campus_teamup.viewmodels.ViewProfileViewModel
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.NotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ViewUserProfiles(
    viewProfileViewModel: ViewProfileViewModel,
    notificationViewModel: NotificationViewModel,
    receiverId: String?,
    receiverPhoneNumber: String?,
    receiverName: String?
) {

    val activity = LocalContext.current as? Activity

    val senderId = notificationViewModel.senderId.collectAsState()

    val isRequestAlreadySent by notificationViewModel.isRequestAlreadySend.collectAsState()
    val isChatRoomAlreadyCreated = notificationViewModel.isChatRoomAlreadyCreated.collectAsState()

    var showRequestDialog by remember {
        mutableStateOf(false)
    }

    if (showRequestDialog) {
        ShowRequestDialog(notificationViewModel,
            onCancel = {
                showRequestDialog = false
            },
            onConfirm = {
                notificationViewModel.fetchReceiverFCMToken(receiverId!! , onFcmFetched = { // first fetch fcm

                    notificationViewModel.sendNotification("New Request","Team is interested in your profile", receiverId)  // send notification
                    showRequestDialog = false
                })
            },
        )
    }




    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(stringResource(R.string.user_profile))
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFEDF9FE)
            ),
            navigationIcon = {
                IconButton(onClick = {
                    activity?.finish()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.browseback),
                        contentDescription = null,
                        tint = Black
                    )
                }
            }
        )
    }) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = BackgroundGradientColor,
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            FloatingBubbles()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {


                ViewUserImage(viewProfileViewModel, notificationViewModel, receiverName , receiverId)
                ViewEducationDetails(
                    modifier = Modifier,
                    viewProfileViewModel,
                )

                ViewSkills(
                    modifier = Modifier,
                    viewProfileViewModel
                )

                ViewCodingProfiles(
                    modifier = Modifier,
                    viewProfileViewModel
                )


                if(senderId.value == receiverId)
                    Text(text = "" , color = White , style = MaterialTheme.typography.titleMedium)
                else if(isRequestAlreadySent || isChatRoomAlreadyCreated.value){
                    Text(text = if(isChatRoomAlreadyCreated.value) "Request Accepted" else "Request Sent"  , color = White , style = MaterialTheme.typography.titleMedium)
                }
                else{
                    Button(
                        onClick = {
                            showRequestDialog = true
                        },
                        enabled = !isChatRoomAlreadyCreated.value,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6C63FF)
                        ),
                        shape = RoundedCornerShape(25.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        )
                    ) {

                        Text(text = "Send Request")
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))

            }
        }
    }
}
