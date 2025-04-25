package com.example.campus_teamup.saveditems

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.LoadAnimation
import com.example.campus_teamup.helper.rememberNetworkStatus
import com.example.campus_teamup.myactivities.UserData
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.HomeScreenViewModel
import com.example.campus_teamup.viewmodels.SavedItemsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSavedItems(currentUserData: UserData?, savedItemsViewModel: SavedItemsViewModel) {
    Log.d("SavedItems", "User data in show saved items is ${currentUserData?.userId} <-")
    val bgColor = BackGroundColor
    val textColor = White
    val activity = LocalContext.current as? Activity
    val isConnected = rememberNetworkStatus()
    val snackbarHostState = remember { SnackbarHostState() }

    val savedProjectList = savedItemsViewModel.showProjectList.collectAsState()
    val savedRolesList = savedItemsViewModel.savedRolesList.collectAsState()
    val savedVacancyList = savedItemsViewModel.savedVacancyList.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.saved_items)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        activity?.finish()
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

                val (divider, savedRoles, savedVacancy, savedProjects, projectList, rolesList, savedItemsArea) = createRefs()

                HorizontalDivider(modifier = Modifier
                    .fillMaxWidth()
                    .background(BorderColor)
                    .constrainAs(divider) {})

                // SAVED ROLES , SAVED VACANCY , SAVED PROJECTS


                if (isConnected) {


                OutlinedButton(onClick = {
                    navController.navigate("savedRoles") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentDestination == "savedRoles") White else BackGroundColor
                ), modifier = Modifier.constrainAs(savedRoles) {
                    top.linkTo(divider.bottom, margin = 6.dp)
                }) {
                    Text(
                        text = stringResource(id = R.string.roles),
                        color = if (currentDestination == "savedRoles") Black else White
                    )
                }
                OutlinedButton(onClick = {
                    navController.navigate("savedVacancy") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentDestination == "savedVacancy") White else BackGroundColor
                ), modifier = Modifier.constrainAs(savedVacancy) {
                    top.linkTo(divider.bottom, margin = 6.dp)
                }) {
                    Text(
                        text = stringResource(id = R.string.vacancies),
                        color = if (currentDestination == "savedVacancy") Black else White
                    )
                }
                OutlinedButton(onClick = {
                    navController.navigate("savedProjects") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentDestination == "savedProjects") White else BackGroundColor
                ), modifier = Modifier.constrainAs(savedProjects) {
                    top.linkTo(divider.bottom, margin = 6.dp)
                }) {
                    Text(
                        text = stringResource(id = R.string.projects),
                        color = if (currentDestination == "savedProjects") Black else White
                    )
                }
                createHorizontalChain(
                    savedRoles,
                    savedVacancy,
                    savedProjects,
                    chainStyle = ChainStyle.Spread
                )





                    ConstraintLayout(modifier = Modifier
                        .constrainAs(savedItemsArea) {
                            top.linkTo(savedProjects.bottom, margin = 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxSize()) {
                        NavHost(navController, startDestination = "savedRoles") {
                            composable("savedRoles") {
                                ShowSavedRoles(savedRolesList, onRoleUnsave = { roleId ->
                                    savedItemsViewModel.unSaveRole(
                                        roleId,
                                        currentUserData?.phoneNumber
                                    )
                                })
                            }
                            composable("savedVacancy") {
                                ShowSavedVacancies(
                                    savedVacancyList,
                                    onVacancyUnsave = { vacancyId ->
                                        savedItemsViewModel.unSaveVacancy(
                                            vacancyId,
                                            currentUserData?.phoneNumber
                                        )
                                    })
                            }
                            composable("savedProjects") {
                                Log.d(
                                    "FetchingProjects",
                                    "In ShowSavedItems size is ${savedProjectList.value.size}"
                                )
                                ShowSavedProjects(
                                    savedProjectList,
                                    onProjectUnsave = {
                                        savedItemsViewModel.unSaveProject(
                                            it,
                                            currentUserData?.phoneNumber
                                        )
                                    })
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .background(BackGroundColor), contentAlignment = Alignment.Center
                    ) {
                        LoadAnimation(
                            modifier = Modifier.size(200.dp),
                            animation = R.raw.nonetwork,
                            playAnimation = true
                        )
                    }
                }

            }
        })
}