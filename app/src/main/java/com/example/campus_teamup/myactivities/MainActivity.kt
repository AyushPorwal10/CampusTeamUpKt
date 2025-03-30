package com.example.campus_teamup.myactivities

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
import com.example.campus_teamup.screens.HomeScreen
import com.example.campus_teamup.ui.theme.BackGroundColor

import com.example.campus_teamup.viewmodels.HomeScreenViewModel
import com.example.campus_teamup.viewmodels.SearchRoleVacancy
import com.example.campus_teamup.viewmodels.UserDataSharedViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeScreenViewModel: HomeScreenViewModel by viewModels()
    private val searchRoleVacancy: SearchRoleVacancy by viewModels()
    private val userDataSharedViewModel : UserDataSharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Post", "MainActivity Created")




        setContent {
            val userData = userDataSharedViewModel.userData.collectAsState()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackGroundColor)
            ) {
                    HomeScreen(this@MainActivity, homeScreenViewModel, searchRoleVacancy , userData.value?.userId)


            }
        }

        homeScreenViewModel.fetchUserData()
        homeScreenViewModel.saveFCMToken()

        homeScreenViewModel.fetchInitialOrPaginatedRoles()
        homeScreenViewModel.fetchInitialOrPaginatedVacancy()
    }


    private fun checkGooglePlayServices(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)

        return if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, 2404)?.show()
            } else {
                Log.e("FCM", "Google Play Services not available on this device.")
                finish() // Close the app if necessary
            }
            false
        } else {
            true
        }
    }


    override fun onPause() {
        super.onPause()
        Log.d("Activity", "MainActivity is on Pause")
    }

    override fun onResume() {
        super.onResume()

        checkGooglePlayServices()

    }
}


