package com.example.new_campus_teamup.vacancy.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.Dimensions
import com.example.new_campus_teamup.helper.ShowRequestDialog
import com.example.new_campus_teamup.helper.TimeAndDate
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myAnimation.FloatingBubbles
import com.example.new_campus_teamup.myactivities.UserData
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.BluePrimary
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.ButtonColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.ViewVacancyViewModel
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewVacancyDetails(
    modifier: Modifier = Modifier,
    vacancy: VacancyDetails,
    currentUserData: State<UserData?>,
    viewVacancyViewModel: ViewVacancyViewModel
) {

    val context = LocalContext.current
    val showRequestDialog = remember { mutableStateOf(false) }
    val isChatRoomAlreadyCreated = viewVacancyViewModel.isChatRoomAlreadyCreated.collectAsState()
    val isRequestAlreadySent = viewVacancyViewModel.isRequestSent.collectAsState()




    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF6C63FF),
                                Color(0xFF8B7CF8)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        ),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF6C63FF), Color(0xFF8B7CF8))
                            ),
                            shape = CircleShape
                        )
                        .padding(3.dp)
                        .background(Color.White, CircleShape)
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {

                    AsyncImage(
                        model = vacancy.teamLogo.ifBlank { R.drawable.vacancies },
                        contentDescription = "Team Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )

                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Team : ${vacancy.teamName}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                VacancyInfoRow(
                    icon = R.drawable.roles,
                    label = "Looking For",
                    value = vacancy.roleLookingFor,
                    iconColor = Color(0xFF38A169)
                )

                Spacer(modifier = Modifier.height(12.dp))

                VacancyInfoRow(
                    icon = R.drawable.hackathon,
                    label = "Hackathon",
                    value = vacancy.hackathonName,
                    iconColor = Color(0xFFD69E2E)
                )

                Spacer(modifier = Modifier.height(12.dp))

                VacancyInfoRow(
                    icon = R.drawable.skills,
                    label = "Skills Required",
                    value = vacancy.skills,
                    iconColor = Color(0xFF6C63FF),
                    isHighlighted = true
                )

                Spacer(modifier = Modifier.height(16.dp))
                if(vacancy.roleDescription.isNotEmpty()){
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF7FAFC)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Role Description: ${vacancy.roleDescription}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF718096)
                            ),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.clock),
                            contentDescription = "Time",
                            tint = Color(0xFF718096),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Posted ${TimeAndDate.getTimeAgoFromDate(vacancy.postedOn)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF718096)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))




                if (currentUserData.value?.userId != vacancy.postedBy) {
                    Button(
                        onClick = {
                            showRequestDialog.value = true
                        },
                        enabled = !isRequestAlreadySent.value && !isChatRoomAlreadyCreated.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6C63FF)
                        ),
                        shape = RoundedCornerShape(25.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        )
                    ) {

                        if(isRequestAlreadySent.value || isChatRoomAlreadyCreated.value){
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Accept",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Text(
                            text = if (isRequestAlreadySent.value) stringResource(id = R.string.request_already_sent)
                            else if (isChatRoomAlreadyCreated.value) stringResource(id = R.string.request_accepted) else stringResource(
                                id = R.string.send_request
                            ), color = White, style = MaterialTheme.typography.titleMedium
                        )
                    }







                    if (showRequestDialog.value) {
                        ShowRequestDialog(viewVacancyViewModel, onCancel = {
                            showRequestDialog.value = false
                        }) {
                            Log.d("VacancyNotification", "Confirm clicked")

                            Log.d(
                                "VacancyNotification",
                                "Current user id is ${currentUserData.value?.userId} <-"
                            )

                            currentUserData.value?.userId?.let {

                                viewVacancyViewModel.sendNotification(
                                    currentUserData.value?.phoneNumber!!,
                                    it,
                                    currentUserData.value?.userName!!,
                                    onNotificationSent = {
                                        showRequestDialog.value = false
                                    },
                                    onNotificationError = {
                                        showRequestDialog.value = false
                                        ToastHelper.showToast(
                                            context,
                                            "Sorry Something went wrong !"
                                        )
                                    },
                                    vacancy.postedBy,
                                    vacancy.postedBy
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun VacancyInfoRow(
    icon: Int,
    label: String,
    value: String,
    iconColor: Color,
    isHighlighted: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    iconColor.copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF718096),
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isHighlighted) Color(0xFF6C63FF) else Color(0xFF2D3748),
                    fontWeight = if (isHighlighted) FontWeight.SemiBold else FontWeight.Normal
                )
            )
        }
    }
}


