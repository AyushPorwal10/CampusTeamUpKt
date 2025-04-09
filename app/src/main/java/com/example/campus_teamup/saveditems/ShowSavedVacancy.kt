package com.example.campus_teamup.saveditems

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.example.campus_teamup.roleprofile.screens.SingleRole
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.vacancy.screens.SingleVacancy

@Composable
fun ShowSavedVacancies(savedVacancy : State<List<VacancyDetails>> , onVacancyUnsave: (String) -> Unit) {
    Log.d("FetchingVacancy","In lazy it is ${savedVacancy.value.size}")

    LazyColumn(modifier  = Modifier.fillMaxWidth().background(BackGroundColor) , horizontalAlignment = Alignment.CenterHorizontally){
        items(savedVacancy.value){vacancy->
            Log.d("FetchingVacancy","Single Vacancy loads")
            SingleVacancy(modifier = Modifier, vacancy , onSaveVacancy ={
                onVacancyUnsave(it.vacancyId)
            } ,true)
        }
    }
}