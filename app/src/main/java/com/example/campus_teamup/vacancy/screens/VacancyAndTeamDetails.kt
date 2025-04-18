package com.example.campus_teamup.vacancy.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.LoadAnimation
import com.example.campus_teamup.helper.rememberNetworkStatus
import com.example.campus_teamup.myactivities.UserData
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.ViewVacancyViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun VacancyAndTeamDetails(
    vacancyDetails: VacancyDetails,
    viewVacancyViewModel: ViewVacancyViewModel,
    currentUserData: State<UserData?>,
) {

    // reusing viewmodel of role profile



    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = stringResource(id = R.string.app_name), color = White
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackGroundColor, navigationIconContentColor = White
        ), navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.browseback),
                    contentDescription = null,
                    tint = White
                )
            }
        })
    }) { paddingValues ->




            ConstraintLayout(
                modifier = Modifier
                    .background(BackGroundColor)
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                val (divider,vacancyDetailsBtn,vacancyDetailsArea) = createRefs()



                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BorderColor)
                        .constrainAs(divider) {

                        })

                ViewVacancyDetails(
                    modifier = Modifier
                        .padding(20.dp)
                        .constrainAs(vacancyDetailsArea) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(vacancyDetailsBtn.bottom, margin = 20.dp)
                        },
                    vacancyDetails
                    ,currentUserData,
                    viewVacancyViewModel
                )

            }


    }


}
