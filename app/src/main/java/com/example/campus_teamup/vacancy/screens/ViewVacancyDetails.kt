package com.example.campus_teamup.vacancy.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.Dimensions
import com.example.campus_teamup.helper.ShowRequestDialog
import com.example.campus_teamup.helper.TimeAndDate
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.helper.rememberNetworkStatus
import com.example.campus_teamup.myactivities.UserData
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.example.campus_teamup.ui.theme.BluePrimary
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.ViewVacancyViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewVacancyDetails(
    modifier: Modifier = Modifier,
    vacancy: VacancyDetails,
    currentUserData: State<UserData?>,
    viewVacancyViewModel: ViewVacancyViewModel
) {
    val textColor = White
    val context = LocalContext.current


    val isConnected = rememberNetworkStatus()
    val snackbarHostState = remember { SnackbarHostState() }


    val showRequestDialog = remember { mutableStateOf(false) }
    Log.d(
        "VacancyNotification",
        "Current user id in viewvacancydetails is  is ${currentUserData.value?.userId} <- "
    )

    val isChatRoomAlreadyCreated = viewVacancyViewModel.isChatRoomAlreadyCreated.collectAsState()

    val isRequestAlreadySent = viewVacancyViewModel.isRequestSent.collectAsState()


    LaunchedEffect(isConnected) {
        if (!isConnected) {
            snackbarHostState.showSnackbar(
                message = "No Internet Connection",
                actionLabel = "OK"
            )
        }
    }

    Box(
        modifier = modifier
            .border(
                0.5.dp, BorderColor,
                shape = RoundedCornerShape(Dimensions.largeRoundedShape)
            )
            .fillMaxWidth(0.98f), contentAlignment = Alignment.Center
    ) {



        ConstraintLayout(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth()
        ) {
            val (teamLogo, teamName, roleLookingFor, hackathonName, roleDescription, datePosted, skillRequired, sendRequestBtn) = createRefs()





            AsyncImage(
                model = vacancy.teamLogo,
                contentDescription = "Team Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .constrainAs(teamLogo) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, White, CircleShape))

            Text(text = "Team : ${vacancy.teamName}",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.constrainAs(teamName)
                {
                    top.linkTo(teamLogo.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Text(
                text = "Looking For : ${vacancy.roleLookingFor}",
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                style = MaterialTheme.typography.titleMedium,
                color = LightTextColor,
                modifier = Modifier.constrainAs(roleLookingFor) {
                    top.linkTo(teamName.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                })

            Text(text = "Hackathon : ${vacancy.hackathonName}", color = LightTextColor,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(hackathonName) {
                        top.linkTo(roleLookingFor.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    })

            Text(text = "Skills Required : ${vacancy.skills}", color = BluePrimary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(skillRequired) {
                        top.linkTo(hackathonName.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    })

            Text(text = "Role Description : " + vacancy.roleDescription.ifEmpty { "No Description" },
                color = LightTextColor,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(roleDescription) {
                        top.linkTo(skillRequired.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    })

            Text(text = "Posted ${TimeAndDate.getTimeAgoFromDate(vacancy.postedOn)}", color = White,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(datePosted) {
                        top.linkTo(roleDescription.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })


            // if id are not equal than show btn



            if(isConnected){
                if (currentUserData.value?.userId != vacancy.postedBy) {
                    OutlinedButton(onClick = {
                        showRequestDialog.value = true
                    }, modifier = Modifier.constrainAs(sendRequestBtn) {
                        top.linkTo(datePosted.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }, enabled = !isRequestAlreadySent.value && !isChatRoomAlreadyCreated.value) {
                        Text(
                            text = if (isRequestAlreadySent.value) stringResource(id = R.string.request_already_sent)
                            else if (isChatRoomAlreadyCreated.value) stringResource(id = R.string.request_accepted)  else  stringResource(
                                id = R.string.send_request
                            ), color = White, style = MaterialTheme.typography.titleMedium
                        )
                    }

                    if (showRequestDialog.value) {
                        ShowRequestDialog(viewVacancyViewModel ,  onCancel = {
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
                                    it, currentUserData.value?.userName!!, onNotificationSent = {
                                        showRequestDialog.value = false
                                    }, onNotificationError = {
                                        showRequestDialog.value = false
                                        ToastHelper.showToast(context, "Sorry Something went wrong !")
                                    }, vacancy.postedBy,
                                    vacancy.phoneNumber
                                )
                            }
                        }
                    }
                }
            }



        }


    }
}


