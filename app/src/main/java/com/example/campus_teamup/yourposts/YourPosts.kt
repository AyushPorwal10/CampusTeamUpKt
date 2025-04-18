package com.example.campus_teamup.yourposts


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.LoadAnimation
import com.example.campus_teamup.helper.rememberNetworkStatus
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourPost(yourPostViewModel: YourPostViewModel) {
    val bgColor = BackGroundColor
    val textColor = White

    val isConnected = rememberNetworkStatus()
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.your_posts)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                ),
                navigationIcon = {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.browseback),
                            contentDescription = null,
                            tint = textColor
                        )
                    }
                },

                )
        }, content = { paddingValues ->


            LaunchedEffect(isConnected) {
                if (!isConnected) {
                    snackbarHostState.showSnackbar(
                        message = "No Internet Connection",
                        actionLabel = "OK"
                    )
                }
            }

            val navController = rememberNavController()

            val currentDestination =
                navController.currentBackStackEntryAsState().value?.destination?.route

            ConstraintLayout(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(BackGroundColor)
                    .fillMaxSize()
            ) {

                val (divider, postedRoles, postedVacancy, postedProjects, postedItemsArea) = createRefs()

                HorizontalDivider(modifier = Modifier
                    .fillMaxWidth()
                    .background(BorderColor)
                    .constrainAs(divider) {})

                // SAVED ROLES , SAVED VACANCY , SAVED PROJECTS

                OutlinedButton(onClick = {
                    navController.navigate("postedRoles"){
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentDestination == "postedRoles") White else BackGroundColor
                ), modifier = Modifier.constrainAs(postedRoles) {
                    top.linkTo(divider.bottom, margin = 6.dp)
                }) {
                    Text(
                        text = stringResource(id = R.string.roles),
                        color = if (currentDestination == "postedRoles") Black else White
                    )
                }
                OutlinedButton(onClick = {
                    navController.navigate("postedVacancy"){
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentDestination == "postedVacancy") White else BackGroundColor
                ), modifier = Modifier.constrainAs(postedVacancy) {
                    top.linkTo(divider.bottom, margin = 6.dp)
                }) {
                    Text(
                        text = stringResource(id = R.string.vacancies),
                        color = if (currentDestination == "postedVacancy") Black else White
                    )
                }
                OutlinedButton(onClick = {
                    navController.navigate("postedProjects"){
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentDestination == "postedProjects") White else BackGroundColor
                ), modifier = Modifier.constrainAs(postedProjects) {
                    top.linkTo(divider.bottom, margin = 6.dp)
                }) {
                    Text(
                        text = stringResource(id = R.string.projects),
                        color = if (currentDestination == "postedProjects") Black else White
                    )
                }
                createHorizontalChain(
                    postedRoles,
                    postedVacancy,
                    postedProjects,
                    chainStyle = ChainStyle.Spread
                )




                if(isConnected){
                    ConstraintLayout(modifier = Modifier
                        .constrainAs(postedItemsArea) {
                            top.linkTo(postedProjects.bottom, margin = 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxSize()) {
                        NavHost(navController, startDestination = "postedRoles") {
                            composable("postedRoles") {
                                ShowPostedRoles(yourPostViewModel)
                            }
                            composable("postedVacancy") {
                                ShowPostedVacancy(yourPostViewModel)
                            }
                            composable("postedProjects") {
                                ShowPostedProjects(yourPostViewModel)
                            }
                        }
                    }
                }
                else {
                    Box(modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(BackGroundColor) , contentAlignment = Alignment.Center){
                        LoadAnimation(modifier = Modifier.size(200.dp) , animation = R.raw.otp, playAnimation = true)
                    }
                }

            }
        })
}