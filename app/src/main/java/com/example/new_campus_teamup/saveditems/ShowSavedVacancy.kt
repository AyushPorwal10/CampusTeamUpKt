package com.example.new_campus_teamup.saveditems

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.vacancy.screens.SingleVacancyCard

@Composable
fun ShowSavedVacancies(
    savedVacancy: State<List<VacancyDetails>>,
    onVacancyUnsave: (String) -> Unit
) {
    Log.d("FetchingVacancy", "In lazy it is ${savedVacancy.value.size}")

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(savedVacancy.value) { vacancy ->
            Log.d("FetchingVacancy", "Single Vacancy loads")
            SingleVacancyCard(modifier = Modifier, vacancy, onSaveVacancy = {
                onVacancyUnsave(it.vacancyId)
            }, true)
        }

        item {
            if (savedVacancy.value.isEmpty()) {
                Box(contentAlignment = Alignment.Center) {
                    LoadAnimation(
                        modifier = Modifier.size(200.dp),
                        animation = R.raw.noresult,
                        playAnimation = true
                    )
                }
            }
        }
    }
}