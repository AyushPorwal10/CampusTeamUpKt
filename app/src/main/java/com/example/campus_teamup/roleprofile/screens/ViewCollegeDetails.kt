package com.example.campus_teamup.roleprofile.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.ShowRequestDialog
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.viewmodels.ViewProfileViewModel
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.NotificationViewModel

@Composable
fun ViewCollegeDetails(
    modifier: Modifier,
    viewProfileViewModel: ViewProfileViewModel,
    receiverId: String?
) {

    val notification: NotificationViewModel = hiltViewModel()
    Log.d("FCM", "Receiver id in viewcollegeDetails is $receiverId <-")
    val collegeDetails = viewProfileViewModel.collegeDetails.collectAsState()

    var showRequestDialog by remember {
        mutableStateOf(false)
    }


    if (showRequestDialog) {
        ShowRequestDialog(
            onCancel = {
                showRequestDialog = false
            },
            onConfirm = {

                notification.fetchReceiverFCMToken(receiverId!! , onFcmFetched = { // first fetch fcm
                    Log.d("FCM","Going to fetch sender id ")
                    notification.fetchSenderId{                                   // than fetch sender id
                        Log.d("FCM","Going to send notificatoin after fetching sender id")
                        notification.sendNotification("New Request","Team is interested in your profile")  // send notification
                        showRequestDialog = false
                    }
                })
            },
            collegeDetails.value?.userName
        )
    }
    ConstraintLayout(modifier = modifier.fillMaxSize()) {

        val (userImage, courseBranchYear) = createRefs()

        AsyncImage(
            model = collegeDetails.value?.userImageUrl,
            contentDescription = "User Profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(1.dp, White, CircleShape)
                .constrainAs(userImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })



        Column(
            modifier = Modifier.constrainAs(courseBranchYear) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(userImage.bottom, margin = 40.dp)
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {


            ShowDetails(details = collegeDetails.value?.collegeName, icon = R.drawable.college)
            ShowDetails(
                details = "Course : " + collegeDetails.value?.course,
                icon = R.drawable.college
            )
            ShowDetails(
                details = "Branch : " + collegeDetails.value?.branch,
                icon = R.drawable.college
            )
            ShowDetails(
                details = "Graduation year : " + collegeDetails.value?.year,
                icon = R.drawable.college
            )

            // ON click of this notification will be sent to user who posted role
            OutlinedButton(onClick = {
                showRequestDialog = true

            }, colors = ButtonDefaults.buttonColors(containerColor = BackGroundColor)) {
                Text(text = "Send Request")
            }

        }
    }
}

@Composable
fun ShowDetails(details: String?, icon: Int) {
    OutlinedTextField(
        value = details ?: "",
        onValueChange = { },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 2,
        readOnly = true,
        leadingIcon = {
            Icon(
                painterResource(id = icon), contentDescription = "",
                modifier = Modifier.size(22.dp), tint = White
            )
        }, modifier = Modifier.fillMaxWidth(0.85f)
    )
}