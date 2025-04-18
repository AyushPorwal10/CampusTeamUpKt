package com.example.campus_teamup.viewnotifications

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.myactivities.UserData
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.ViewNotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TeamJointNotification(
    memberInviteNotification: NotificationItems.MemberInviteNotification,
    index: Int,
    viewNotificationViewModel: ViewNotificationViewModel,
    currentUserData: UserData?
) {


    Log.d(
        "DenyRequest", "In Composable memberRequestId is " +
                "senderId is ${memberInviteNotification.senderId}" + "senderName is ${memberInviteNotification.senderName}"
    )

    val textColor = LightTextColor
    val bgColor = BackGroundColor

    Card(
        colors = CardDefaults.cardColors(containerColor = BorderColor, contentColor = White),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(BorderColor)
    ) {

        ConstraintLayout(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(1f)
        ) {
            val (userImage, userName, acceptBtn, rejectBtn, viewProfileBtn) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                colorFilter = ColorFilter.tint(textColor),
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    })

            Text(
                text = "${memberInviteNotification.senderName} wants to join your Team.",
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                style = MaterialTheme.typography.titleMedium,
                color = White,
                overflow = TextOverflow.Ellipsis,

                fontSize = 18.sp,
                modifier = Modifier
                    .constrainAs(userName) {
                        top.linkTo(userImage.top)
                        start.linkTo(userImage.end, margin = 2.dp)
                        bottom.linkTo(userImage.bottom)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(0.75f))

            TextButton(onClick = {

            }, modifier = Modifier.constrainAs(viewProfileBtn) {
                top.linkTo(userName.bottom, margin = 6.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                Text(
                    text = "View Profile",
                    color = White,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.acceptbtn),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .size(28.dp)
                    .constrainAs(acceptBtn) {
                        top.linkTo(viewProfileBtn.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        Log.d("VacanyRequest","Index is $index")
                        Log.d("VacancyRequest","Going to accept request")
                        viewNotificationViewModel.userRequestCreateChatRoom(index, currentUserData?.userId ,currentUserData?.userName ,  currentUserData?.phoneNumber)
                    }, tint = textColor)

            Icon(
                painter = painterResource(id = R.drawable.rejectbtn),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .size(28.dp)
                    .clickable {
                        viewNotificationViewModel.denyUserRequest(index, currentUserData?.userId , currentUserData?.phoneNumber)
                    }
                    .constrainAs(rejectBtn) {
                        top.linkTo(viewProfileBtn.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)


                    }, tint = textColor)
            createHorizontalChain(acceptBtn, rejectBtn, chainStyle = ChainStyle.Spread)
        }
    }
}