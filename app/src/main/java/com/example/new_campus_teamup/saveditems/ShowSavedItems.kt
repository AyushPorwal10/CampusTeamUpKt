package com.example.new_campus_teamup.saveditems

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myactivities.UserData
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightBlueColor
import com.example.new_campus_teamup.ui.theme.TopAppBarColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.SavedItemsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSavedItems(
    currentUserData: UserData?,
    savedItemsViewModel: SavedItemsViewModel
) {
    val context  = LocalContext.current

    val bgColor = BackGroundColor
    val textColor = White
    val activity = LocalContext.current as? Activity

    val snackbarHostState = remember { SnackbarHostState() }

    val savedProjectList = savedItemsViewModel.showProjectList.collectAsState()
    val savedRolesList = savedItemsViewModel.savedRolesList.collectAsState()
    val savedVacancyList = savedItemsViewModel.savedVacancyList.collectAsState()

    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route



    LaunchedEffect(Unit){
        savedItemsViewModel.errorMessage.collect{error->
            error?.let {
                ToastHelper.showToast(context ,error)
                savedItemsViewModel.clearError()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.saved_items) , fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TopAppBarColor,
                    titleContentColor = Black,
                    navigationIconContentColor = Black
                ),
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.browseback),
                            contentDescription = null,
                            tint = Black
                        )
                    }
                }
            )
        },
        content = { paddingValues ->





            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = BackgroundGradientColor
                        )
                    )
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BorderColor)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = {
                            navController.navigate("savedRoles") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentDestination == "savedRoles")  LightBlueColor else Color.LightGray
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.roles),
                            color = if (currentDestination == "savedRoles") White else Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            navController.navigate("savedVacancy") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentDestination == "savedVacancy") LightBlueColor else Color.LightGray
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.vacancies),
                            color = if (currentDestination == "savedVacancy")  White else Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            navController.navigate("savedProjects") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentDestination == "savedProjects") LightBlueColor else Color.LightGray
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.projects),
                            color = if (currentDestination == "savedProjects")  White else Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    NavHost(navController, startDestination = "savedRoles") {
                        composable("savedRoles") {
                            ShowSavedRoles(
                                savedRolesList,
                                onRoleUnsave = { roleId ->
                                    savedItemsViewModel.unSaveRole(
                                        roleId,
                                        currentUserData?.userId
                                    )
                                }
                            )
                        }
                        composable("savedVacancy") {
                            ShowSavedVacancies(
                                savedVacancyList,
                                onVacancyUnsave = { vacancyId ->
                                    savedItemsViewModel.unSaveVacancy(
                                        vacancyId,
                                        currentUserData?.userId
                                    )
                                }
                            )
                        }
                        composable("savedProjects") {
                            ShowSavedProjects(
                                savedProjectList,
                                onProjectUnsave = {
                                    savedItemsViewModel.unSaveProject(
                                        it,
                                        currentUserData?.userId
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}
