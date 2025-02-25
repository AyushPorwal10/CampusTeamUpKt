package com.example.campus_teamup.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.example.campus_teamup.mytesting.SingleVacancy
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.HomeScreenViewModel
import com.example.campus_teamup.viewmodels.SearchRoleVacancy
import kotlinx.coroutines.delay

@Composable
fun VacanciesScreen(homeScreenViewModel: HomeScreenViewModel,
                    searchRoleVacancy: SearchRoleVacancy) {
    val textColor = White
    val bgColor = BackGroundColor


    val searchText by searchRoleVacancy.searchVacancyText.collectAsState()

    val searchOption = listOf("Search by Team Name" , "Search by Role" , "Search by Hackathon")
    var placeHolderIndex by remember {
        mutableIntStateOf(0)
    }

    val vacancies by if (searchText.isNotEmpty()){
        searchRoleVacancy.searchedVacancies.collectAsState()
    }
    else{
        homeScreenViewModel.vacancyStateFlow.collectAsState()
    }

    LaunchedEffect(Unit){
        while (true){
            delay(2000)
            placeHolderIndex = (placeHolderIndex + 1) % searchOption.size
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {


        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val (searchBar , filterIcon , divider, vacancyList) = createRefs()

            OutlinedTextField(value = searchText,
                onValueChange = { searchQuery ->
                                searchRoleVacancy.onSearchedVacancyTextChange(searchQuery)
                },
                colors = TextFieldStyle.myTextFieldColor(),
                shape = TextFieldStyle.defaultShape,
                maxLines = 1,
                modifier = Modifier
                    .constrainAs(searchBar) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)

                    }
                    .fillMaxWidth(0.8f),
                placeholder = {
                    Box(
                        modifier = Modifier.animateContentSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = searchOption[placeHolderIndex],
                            style = MaterialTheme.typography.titleMedium,
                            color = textColor
                        )
                    }
                },
                leadingIcon = {
                    Icon(

                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(22.dp)
                    )
                })

            Icon(
                painter = painterResource(id = R.drawable.filter),
                contentDescription = null,
                tint = White,
                modifier = Modifier
                    .size(26.dp)
                    .constrainAs(filterIcon) {
                        start.linkTo(searchBar.end, margin = 8.dp)
                        end.linkTo(parent.end)
                        top.linkTo(searchBar.top)
                        bottom.linkTo(searchBar.bottom)
                    }
                    .fillMaxWidth(0.1f)
            )

            // horizontal arrangement of search items
            createHorizontalChain(searchBar , filterIcon , chainStyle = ChainStyle.Spread)

            HorizontalDivider(thickness = 1.dp,
                color = BorderColor,
                modifier = Modifier.constrainAs(divider){
                    start.linkTo(parent.start)
                    top.linkTo(searchBar.bottom , margin = 16.dp)
                    end.linkTo(parent.end)
                })


            // showing list of vacancies

            ShowListOfVacancies(
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(vacancyList) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(divider.bottom, margin = 5.dp)
                    },
                vacancies,
                homeScreenViewModel
            )





        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowListOfVacancies(
    modifier: Modifier,
    vacancies: List<VacancyDetails>,
    homeScreenViewModel: HomeScreenViewModel
) {

    val isRefreshing = homeScreenViewModel.isVacancyRefreshing.collectAsState()

    LaunchedEffect(Unit){
        Log.d("Vacancy","Composable Fetching When composable loads")
        homeScreenViewModel.observeVacancyInRealTime()
    }
    PullToRefreshBox(isRefreshing = isRefreshing.value, onRefresh = {
        Log.d("Vacancy","Composable Observing new roles user refresh")
        homeScreenViewModel.observeVacancyInRealTime()
    }, modifier = modifier , contentAlignment = Alignment.Center) {

        LazyColumn(modifier = modifier , verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(vacancies){vacancy->
                SingleVacancy(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    vacancy

                )
            }
        }
    }


}
