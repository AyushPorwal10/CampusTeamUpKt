package com.example.new_campus_teamup.viewnotifications

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.helper.ProgressIndicator
import com.example.new_campus_teamup.helper.TimeAndDate
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myactivities.UserData
import com.example.new_campus_teamup.myactivities.ViewUserProfile
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.ViewNotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TeamJointNotification(
    memberInviteNotification: NotificationItems.MemberInviteNotification,
    index: Int,
    viewNotificationViewModel: ViewNotificationViewModel,
    currentUserData: UserData?
) {

    val isChatRoomCreating = viewNotificationViewModel.isChatRoomCreating.collectAsState()

    val context = LocalContext.current


    LaunchedEffect(Unit){
        viewNotificationViewModel.errorMessage.collect{error->
            error?.let {
                ToastHelper.showToast(context ,error)
                viewNotificationViewModel.clearError()
            }
        }
    }


    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6C63FF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(8.dp)
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
                    text = "${memberInviteNotification.senderName} wants to join your Team.",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    color = White,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if(isChatRoomCreating.value){
                        ProgressIndicator.showProgressBar(modifier = Modifier, canShow = isChatRoomCreating.value)
                    }
                    else{
                        IconButton(
                            onClick = {
                                Log.d("VacanyRequest","Index is $index")
                                Log.d("VacancyRequest","Going to accept request")
                                viewNotificationViewModel.userRequestCreateChatRoom(index, currentUserData?.userId ,currentUserData?.userName ,  currentUserData?.phoneNumber , onError = {
                                    ToastHelper.showToast(context , "Something went wrong ! \n Please try again later.")
                                })
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color(0xFFDFFFE1),
                                contentColor = Color(0xFF2E7D32)
                            )
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Accept")
                        }
                    }


                    IconButton(
                        onClick = {
                            viewNotificationViewModel.denyUserRequest(index, currentUserData?.userId , currentUserData?.phoneNumber)
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
                    Text("Sent : ${TimeAndDate.getTimeAgoFromDate(memberInviteNotification.time)}" , color = LightTextColor)
                }

                TextButton(
                    onClick = {
                              val intent = Intent(context ,ViewUserProfile::class.java)
                        intent.putExtra("userId",memberInviteNotification.senderId)
                        intent.putExtra("phone_number" , memberInviteNotification.senderPhoneNumber)
                        intent.putExtra("userName",memberInviteNotification.senderName)
                        context.startActivity(intent)

                    },
                ) {
                    Text("View More" , color = LightTextColor)
                }
            }
        }
    }
}
