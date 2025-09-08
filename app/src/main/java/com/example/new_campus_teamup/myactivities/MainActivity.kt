package com.example.new_campus_teamup.myactivities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.new_campus_teamup.helper.HandleNotificationPermission
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.mysealedClass.BottomNavScreens
import com.example.new_campus_teamup.project.screens.ProjectsScreen
import com.example.new_campus_teamup.roleprofile.screens.RolesScreen
import com.example.new_campus_teamup.screens.homescreens.HomeScreen
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.vacancy.screens.VacanciesScreen

import com.example.new_campus_teamup.viewmodels.HomeScreenViewModel
import com.example.new_campus_teamup.viewmodels.SearchRoleVacancy
import com.example.new_campus_teamup.viewmodels.UserDataSharedViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeScreenViewModel: HomeScreenViewModel by viewModels()
    private val searchRoleVacancy: SearchRoleVacancy by viewModels()
    private val userDataSharedViewModel: UserDataSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupComposeContent()
    }




    private fun setupComposeContent() {
        setContent {

            HandleNotificationPermission(onGranted = {

            },
                onDenied ={

                })
            val context = LocalContext.current
            val userData = userDataSharedViewModel.userData.collectAsState()
            val navController = rememberNavController()


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = BackgroundGradientColor
                        )
                    )
            ) {

            NavHost(navController = navController , startDestination = BottomNavScreens.Home.screen){
                composable(BottomNavScreens.Roles.screen) {
                        RolesScreen(homeScreenViewModel, searchRoleVacancy, navController) { roleDetails ->
                            homeScreenViewModel.saveRole(roleDetails, onRoleSaved = {
                                ToastHelper.showToast(context, "Role Saved Successfully.")
                            }, onError = {
                                ToastHelper.showToast(context, "Something went wrong.")
                            })
                        }
                    }

                composable(BottomNavScreens.Projects.screen) {
                        ProjectsScreen(
                            homeScreenViewModel,
                            navController,
                            saveProject = {
                                homeScreenViewModel.saveProject(it, onProjectSaved = {

                                    ToastHelper.showToast(context, "Project Saved Successfully")
                                },
                                    onError = {

                                        ToastHelper.showToast(context, "Something went wrong.")
                                    })
                            }
                        )
                    }

                composable(BottomNavScreens.Vacancies.screen) {
                        VacanciesScreen(
                            homeScreenViewModel, searchRoleVacancy,
                            navController,
                            saveVacancy = {
                                homeScreenViewModel.saveVacancy(it, onVacancySaved = {

                                    ToastHelper.showToast(context, "Vacancy Saved Successfully.")

                                },
                                    onError = {
                                        ToastHelper.showToast(context, "Something went wrong.")
                                    })
                            }
                        )
                    }

                composable(BottomNavScreens.Home.screen){
                    HomeScreen(homeScreenViewModel, navController , searchRoleVacancy, userData.value?.userId)
                }
            }

            }

            LaunchedEffect(Unit) {
                homeScreenViewModel.fetchUserData()
                homeScreenViewModel.saveFCMToken()
            }

            LaunchedEffect(Unit){
                homeScreenViewModel.errorMessage.collect{error->
                    error?.let {
                        ToastHelper.showToast(context ,error)
                        homeScreenViewModel.clearError()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}

