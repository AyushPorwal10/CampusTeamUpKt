package com.example.campus_teamup.vacancy

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.campus_teamup.R
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.example.campus_teamup.vacancy.screens.VacancyAndTeamDetails

class ViewVacancy : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vacancy = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("vacancy_details", VacancyDetails::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("vacancy_details")
        }

        Log.d("Vacancy", "Received vacacny in activity ${vacancy?.skills}")
        setContent {
            VacancyAndTeamDetails(vacancy!!)
        }
    }
}