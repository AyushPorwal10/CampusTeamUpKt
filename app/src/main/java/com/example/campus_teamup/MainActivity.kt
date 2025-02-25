package com.example.campus_teamup

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.campus_teamup.screens.HomeScreen
import com.example.campus_teamup.ui.theme.BackGroundColor

import com.example.campus_teamup.viewmodels.HomeScreenViewModel
import com.example.campus_teamup.viewmodels.SearchRoleVacancy
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeScreenViewModel : HomeScreenViewModel by viewModels()
    private val searchRoleVacancy : SearchRoleVacancy by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Post","MainActivity Created")


        setContent {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackGroundColor)
                ){
                    HomeScreen(this@MainActivity , homeScreenViewModel , searchRoleVacancy)
                }
            }
        Log.d("Roles","MainActivity is going to fetch new roles")

        homeScreenViewModel.fetchInitialOrPaginatedRoles()
        homeScreenViewModel.fetchInitialOrPaginatedVacancy()
        homeScreenViewModel.fetchProjects()

        }


    }


