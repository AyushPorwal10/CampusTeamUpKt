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
        
        checkGooglePlayServices()
        setupComposeContent()
    }


    private fun checkGooglePlayServices(): Boolean {
        Log.e("GooglePlayServices", "Checking Google Play Services")
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)

        return if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                Log.e("GooglePlayServices", "Google Play Services are available on this device.")
                googleApiAvailability.getErrorDialog(this, resultCode, 2404)?.show()
            } else {
                Log.e("GooglePlayServices", "Google Play Services not available on this device.")
                finish()
            }
            false
        } else {
            Log.e("GooglePlayServices", "Else part is executed")
            true
        }
    }

    private fun setupComposeContent() {
        setContent {

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
                    Log.d("UpdateUiTesting","Inside Navhost")
                        RolesScreen(homeScreenViewModel, searchRoleVacancy) { roleDetails ->
                            homeScreenViewModel.saveRole(roleDetails, onRoleSaved = {
//                                scope.launch {
//                                    snackbarHostState.showSnackbar(
//                                        message = "Role Saved Successfully.",
//                                        actionLabel = "Ok"
//                                    )
//                                }
                                ToastHelper.showToast(context, "Role Saved Successfully.")

                            }, onError = {
                                Log.e("SaveRole", "Error is $it")
                                ToastHelper.showToast(context, "Something went wrong.")
                            })
                        }
                    }

                composable(BottomNavScreens.Projects.screen) {
                        ProjectsScreen(
                            homeScreenViewModel,
                            saveProject = {
                                homeScreenViewModel.saveProject(it, onProjectSaved = {
//                                    scope.launch {
//                                        snackbarHostState.showSnackbar(
//                                            message = "Project Saved Successfully.",
//                                            actionLabel = "Ok"
//                                        )
//                                    }
                                    ToastHelper.showToast(context, "Project Saved Successfully")
                                },
                                    onError = {
//                                        scope.launch {
//                                            snackbarHostState.showSnackbar(message = "Something went wrong.")
//                                        }
                                        ToastHelper.showToast(context, "Something went wrong.")
                                    })
                            }
                        )
                    }

                composable(BottomNavScreens.Vacancies.screen) {
                        VacanciesScreen(
                            homeScreenViewModel, searchRoleVacancy,
                            saveVacancy = {
                                homeScreenViewModel.saveVacancy(it, onVacancySaved = {
//                                    scope.launch {
//                                        snackbarHostState.showSnackbar(
//                                            message = "Vacancy Saved Successfully.",
//                                            actionLabel = "Ok"
//                                        )
//                                    }
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
        Log.d("Navigation", "MainActivity is on Resumed")
        checkGooglePlayServices()
    }
}

