package com.example.new_campus_teamup.vacancy.screens

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.myactivities.UserData
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.TopAppBarColor
import com.example.new_campus_teamup.viewmodels.ViewVacancyViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun VacancyAndTeamDetails(
    vacancyDetails: VacancyDetails,
    viewVacancyViewModel: ViewVacancyViewModel,
    currentUserData: State<UserData?>,
) {


    val activity = LocalContext.current as? Activity

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = stringResource(id = R.string.vacancy_details), color = Black, fontWeight = FontWeight.SemiBold
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TopAppBarColor, navigationIconContentColor = Black
        ), navigationIcon = {
            IconButton(onClick = {
                activity?.finish()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.browseback),
                    contentDescription = null,
                    tint = Black
                )
            }
        })
    }) { paddingValues ->




            ConstraintLayout(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = BackgroundGradientColor
                        )
                    )
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
