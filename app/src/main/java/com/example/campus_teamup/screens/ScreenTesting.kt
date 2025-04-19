package com.example.campus_teamup.screens

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.Dimensions
import com.example.campus_teamup.helper.RejectTeamInviteDialog
import com.example.campus_teamup.helper.TimeAndDate
import com.example.campus_teamup.myactivities.UserData
import com.example.campus_teamup.myactivities.ViewUserProfile
import com.example.campus_teamup.myactivities.ViewVacancy
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BluePrimary
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.ViewNotificationViewModel
import com.example.campus_teamup.viewnotifications.NotificationItems




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TeamInviteNotificationTesting(
    teamInviteNotification: NotificationItems.TeamInviteNotification,
    index: Int,
    viewNotificationViewModel: ViewNotificationViewModel,
    currentUserData: UserData?
) {

    Log.d("UserData","In TeamInviteNotification UserId from datastore is ${currentUserData?.userId} ")

    Log.d("UserNotification", "Sender name is ${teamInviteNotification.senderName} <-")
    Log.d("UserNotification", "Sender id is ${teamInviteNotification.senderId} <-")

    var rejectTeamInviteDialog by remember {
        mutableStateOf(false)
    }

    if (rejectTeamInviteDialog) {
        RejectTeamInviteDialog(onCancel = {
            rejectTeamInviteDialog = false
        }) {
            viewNotificationViewModel.denyTeamRequest(index , currentUserData?.userId, currentUserData?.phoneNumber)
        }
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp , BackGroundColor , RoundedCornerShape(12.dp))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${teamInviteNotification.senderName} " + stringResource(id = R.string.invite_message),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    color = White
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    // This is when user accept to do communication with sender chat option is open for them
                    IconButton(
                        onClick = {
                            viewNotificationViewModel.createChatRoom(index,currentUserData?.userId ,currentUserData?.userName , currentUserData?.phoneNumber)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFFDFFFE1),
                            contentColor = Color(0xFF2E7D32)
                        )
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Accept")
                    }
                    // click on deny means user is not interested in team and not want to be a part of team
                    IconButton(
                        onClick = {
                            rejectTeamInviteDialog = !rejectTeamInviteDialog
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFFFFE1E1),
                            contentColor = Color(0xFFC62828)
                        )
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Reject")
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween , modifier = Modifier.fillMaxWidth()){
                TextButton(
                    onClick = {
                    },
                ) {
                    Text("Sent : ${TimeAndDate.getTimeAgoFromDate(teamInviteNotification.time)}" , color = LightTextColor)
                }

                TextButton(
                    onClick = {

                    },
                ) {
                    Text("View More" , color = LightTextColor)
                }
            }
        }
    }
}
