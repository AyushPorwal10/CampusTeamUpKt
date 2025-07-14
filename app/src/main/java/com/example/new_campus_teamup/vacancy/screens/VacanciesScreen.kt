package com.example.new_campus_teamup.vacancy.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.helper.ShimmerEffect
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.RoleCardSurfaceVariant
import com.example.new_campus_teamup.ui.theme.RoleOnCardSurfaceVariant
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.HomeScreenViewModel
import com.example.new_campus_teamup.viewmodels.SearchRoleVacancy
import kotlinx.coroutines.delay

@Composable
fun VacanciesScreen(
    homeScreenViewModel: HomeScreenViewModel,
    searchRoleVacancy: SearchRoleVacancy,
    saveVacancy: (VacancyDetails) -> Unit
) {
    val textColor = White
    val bgColor = BackGroundColor
    var isFocused by remember { mutableStateOf(false) }

    val idOfSavedVacancy by homeScreenViewModel.listOfSavedVacancy.collectAsState()
    val searchText by searchRoleVacancy.searchVacancyText.collectAsState()

    val searchOption = listOf(
        "Search by Team Name",
        "Search by Role",
        "Search by Hackathon",
        "Search by Skills",
        "Search by College Name"
    )

    var placeHolderIndex by remember { mutableIntStateOf(0) }

    val vacancies by if (searchText.isNotEmpty()) {
        searchRoleVacancy.searchedVacancies.collectAsState()
    } else {
        homeScreenViewModel.vacancyStateFlow.collectAsState()
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            placeHolderIndex = (placeHolderIndex + 1) % searchOption.size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = BackgroundGradientColor
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchQuery ->
                    searchRoleVacancy.onSearchedVacancyTextChange(searchQuery)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .onFocusChanged { isFocused = it.isFocused },
                placeholder = {
                    Box(
                        modifier = Modifier.animateContentSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = searchOption[placeHolderIndex],
                            style = MaterialTheme.typography.titleMedium,
                            color = RoleOnCardSurfaceVariant
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = RoleOnCardSurfaceVariant
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4F46E5),
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = RoleCardSurfaceVariant
                ),
                shape = RoundedCornerShape(16.dp)
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ShowListOfVacancies(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                vacancies = vacancies,
                homeScreenViewModel = homeScreenViewModel,
                saveVacancy = saveVacancy,
                idOfSavedVacancy = idOfSavedVacancy
            )
        }
    }
}

@Composable
fun ShowListOfVacancies(
    modifier: Modifier,
    vacancies: List<VacancyDetails>,
    homeScreenViewModel: HomeScreenViewModel,
    saveVacancy: (VacancyDetails) -> Unit,
    idOfSavedVacancy: List<String>,
) {

    val isVacancyLoading = homeScreenViewModel.isVacancyLoading.collectAsState()

    LaunchedEffect(Unit) {
        Log.d("Vacancy", "Composable Fetching When composable loads")
        homeScreenViewModel.observeVacancyInRealTime()
    }



    val filteredVacancy = vacancies.filter { !idOfSavedVacancy.contains(it.vacancyId) }
        LazyColumn(
            modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            items(filteredVacancy) { vacancy ->
                ShimmerEffect(modifier = modifier, isLoading = isVacancyLoading.value) {
                        SingleVacancyCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                            ,
                            vacancy,
                            onSaveVacancy = {
                                saveVacancy(it)
                            },false
                        )
                }


        }

            item {
                if(vacancies.isEmpty() || (vacancies.size-idOfSavedVacancy.size)==0) {
                    Box( contentAlignment = Alignment.Center) {
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
