package com.example.campus_teamup.vacancy.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campus_teamup.R
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.example.campus_teamup.roleprofile.screens.ViewCollegeDetails
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.userprofile.screens.CollegeDetails
import com.example.campus_teamup.viewmodels.ViewProfileViewModel
import com.example.campus_teamup.viewmodels.ViewVacancyViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun VacancyAndTeamDetails(
    vacancyDetails: VacancyDetails,
    viewVacancyViewModel: ViewVacancyViewModel
) {

    // reusing viewmodel of role profile

    val viewProfileViewModel : ViewProfileViewModel = hiltViewModel()

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    var isClicked by remember { mutableStateOf(false) }

    var selectedLayout by remember {
        mutableStateOf("vacancyDetails")
    }
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

            val (topAppBar, teamDetailsBtn,divider, teamDetailsArea,vacancyDetailsBtn,vacancyDetailsArea) = createRefs()



            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BorderColor)
                    .constrainAs(divider) {

                    })

            // CODING PROFILE BUTTON and COLLEGE DETAILS BUTTON

            VacancyDetailsBtn(modifier = Modifier
                .constrainAs(vacancyDetailsBtn) {
                    top.linkTo(topAppBar.bottom, margin = 20.dp)
                }
                .padding(all = 0.dp)
                .height(34.dp), selectedLayout, onClick = {
                isClicked = !isClicked
                selectedLayout = "vacancyDetails"
            })

            TeamDetailsBtn(modifier = Modifier
                .constrainAs(teamDetailsBtn) {
                    top.linkTo(topAppBar.bottom, margin = 20.dp)
                }
                .padding(all = 0.dp)
                .height(34.dp), selectedLayout, onClick = {
                isClicked = !isClicked
                selectedLayout = "teamDetails"
            })

            createHorizontalChain(
                vacancyDetailsBtn, teamDetailsBtn, chainStyle = ChainStyle.Spread
            )





            when (selectedLayout) {
                "vacancyDetails" -> ViewVacancyDetails(
                    modifier = Modifier
                        .padding(20.dp)
                        .constrainAs(vacancyDetailsArea) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(vacancyDetailsBtn.bottom, margin = 20.dp)
                        },
                    vacancyDetails
                )

                "teamDetails" -> ViewTeamDetails(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize()
                        .constrainAs(teamDetailsArea) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(vacancyDetailsBtn.bottom, margin = 20.dp)
                        },
                    viewVacancyViewModel
                )
            }




        }


    }


}

@Composable
fun TeamDetailsBtn(modifier: Modifier, selectedLayout: String, onClick: () -> Unit) {

    OutlinedButton(
        onClick = onClick, modifier = modifier, colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedLayout == "teamDetails") White else BackGroundColor
        )
    ) {
        Text(
            text = stringResource(id = R.string.teamDetails),
            color = if (selectedLayout == "teamDetails") Black else White
        )
    }
}

@Composable
fun VacancyDetailsBtn(modifier: Modifier, selectedLayout: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick, modifier = modifier, colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedLayout == "vacancyDetails") White else BackGroundColor
        )

    ) {
        Text(
            text = stringResource(id = R.string.vacancies),
            color = if (selectedLayout == "vacancyDetails") Black else White
        )
    }
}