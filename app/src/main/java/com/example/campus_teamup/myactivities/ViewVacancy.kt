package com.example.campus_teamup.myactivities

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.example.campus_teamup.vacancy.screens.VacancyAndTeamDetails
import com.example.campus_teamup.viewmodels.UserDataSharedViewModel
import com.example.campus_teamup.viewmodels.ViewVacancyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewVacancy : ComponentActivity() {

    private val viewVacancyViewModel : ViewVacancyViewModel by viewModels()
    private val userDataSharedViewModel : UserDataSharedViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val vacancy = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("vacancy_details", VacancyDetails::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("vacancy_details")
        }

        Log.d("Vacancy", "Received vacancy in activity ${vacancy?.skills}")
        setContent {
            val currentUserData = userDataSharedViewModel.userData.collectAsState()
            Log.d("VacancyNotification","Current user id in activity is ${currentUserData.value?.userId} <- ")

            VacancyAndTeamDetails(vacancy!! , viewVacancyViewModel , currentUserData)

            LaunchedEffect(currentUserData.value?.userId){
                currentUserData.value?.userId.let {userId->
                    viewVacancyViewModel.checkIfRequestAlreadySent(userId, vacancy.postedBy)
                }
            }
            viewVacancyViewModel.fetchTeamDetails(vacancy.postedBy)
            viewVacancyViewModel.getFcmWhoPostedVacancy(vacancy.postedBy)
        }
    }


}