package com.example.new_campus_teamup.myactivities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.screens.HomeScreen
import com.example.new_campus_teamup.ui.theme.BackGroundColor

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

        checkGooglePlayServices()
        setupComposeContent()
    }


    private fun checkGooglePlayServices(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)

        return if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, 2404)?.show()
            } else {
                Log.e("FCM", "Google Play Services not available on this device.")
                finish()
            }
            false
        } else {
            true
        }
    }

    private fun setupComposeContent() {
        setContent {
            val context = LocalContext.current
            val userData = userDataSharedViewModel.userData.collectAsState()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackGroundColor)
            ) {
                HomeScreen(homeScreenViewModel, searchRoleVacancy, userData.value?.userId)

            }

            LaunchedEffect(Unit) {
                homeScreenViewModel.fetchUserData()
                homeScreenViewModel.saveFCMToken()
                homeScreenViewModel.fetchInitialOrPaginatedRoles()
                homeScreenViewModel.fetchInitialOrPaginatedVacancy()
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

