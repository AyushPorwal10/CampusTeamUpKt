package com.example.campus_teamup.viewnotifications

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.RejectTeamInviteDialog
import com.example.campus_teamup.myactivities.UserData
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.ViewNotificationViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TeamInviteNotification(
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


    Card(colors = CardDefaults.cardColors(containerColor = BorderColor, contentColor = White)) {
        ConstraintLayout(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(0.9f)
        ) {
            val (heading, invitationMessage, viewTeamDetailsBtn, acceptBtn, denyBtn) = createRefs()

            Text(
                text = stringResource(id = R.string.team_invite),
                modifier = Modifier.constrainAs(heading) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                color = White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "${teamInviteNotification.senderName} " + stringResource(id = R.string.invite_message),
                maxLines = 2,
                modifier = Modifier.constrainAs(invitationMessage) {
                    top.linkTo(heading.bottom, margin = 4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                color = White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            TextButton(onClick = {

            }, modifier = Modifier.constrainAs(viewTeamDetailsBtn) {
                top.linkTo(invitationMessage.bottom, margin = 4.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                Text(
                    text = stringResource(id = R.string.view_team_details),
                    style = MaterialTheme.typography.titleSmall,
                    color = LightTextColor
                )
            }

            // This is when user accept to do communication with sender chat option is open for them

            IconButton(onClick = {
                viewNotificationViewModel.createChatRoom(index,currentUserData?.userId ,currentUserData?.userName , currentUserData?.phoneNumber)
            }, modifier = Modifier.constrainAs(acceptBtn) {
                top.linkTo(viewTeamDetailsBtn.bottom, margin = 4.dp)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.acceptbtn),
                    contentDescription = null,
                    tint = White
                )
            }
            // click on deny means user is not interested in team and not want to be a part of team

            IconButton(onClick = {
                rejectTeamInviteDialog = !rejectTeamInviteDialog
            }, modifier = Modifier.constrainAs(denyBtn) {
                top.linkTo(viewTeamDetailsBtn.bottom, margin = 4.dp)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.rejectbtn),
                    contentDescription = null,
                    tint = White
                )
            }

            createHorizontalChain(acceptBtn, denyBtn, chainStyle = ChainStyle.Spread)
        }
    }
}