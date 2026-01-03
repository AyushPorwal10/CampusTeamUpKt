package com.example.new_campus_teamup.vacancy.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.UiState
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.helper.ReportPostDialog
import com.example.new_campus_teamup.helper.ShimmerEffect
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.room.VacancyEntity
import com.example.new_campus_teamup.screens.homescreens.CustomRoundedCorner
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.RoleCardSurfaceVariant
import com.example.new_campus_teamup.ui.theme.RoleOnCardSurfaceVariant
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.HomeScreenViewModel
import com.example.new_campus_teamup.viewmodels.SearchRoleVacancy
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacanciesScreen(
    homeScreenViewModel: HomeScreenViewModel,
    searchRoleVacancy: SearchRoleVacancy,
    navController: NavController,
    saveVacancy: (VacancyDetails) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    val searchText by searchRoleVacancy.searchVacancyText.collectAsStateWithLifecycle()

    var vacancyIdToReport by remember { mutableStateOf<String?>(null) }
    val reportPostUiState by homeScreenViewModel.reportPostUiState.collectAsStateWithLifecycle()
    var showReportDialog by remember { mutableStateOf(false) }


    val idsOfSavedVacancy by homeScreenViewModel.listOfSavedVacancy.collectAsStateWithLifecycle()

    val context = LocalContext.current
    if(reportPostUiState is UiState.Success){
        showReportDialog = false
        homeScreenViewModel.resetReportPostState()
    }
    else if(reportPostUiState is UiState.Error){
        showReportDialog = false
        homeScreenViewModel.resetReportPostState()
        ToastHelper.showToast(context, stringResource(R.string.something_went_wrong_try_again))
    }


    ReportPostDialog(showReportDialog,isLoading = reportPostUiState is UiState.Loading, onDismiss = {
        showReportDialog = false
    }, onConfirm = {
        homeScreenViewModel.reportPost("vacancies" , vacancyIdToReport!!)
        vacancyIdToReport = null
    })


    LaunchedEffect(Unit) {
        homeScreenViewModel.observeVacancyInRealTime()
    }

    val searchOption = listOf(
        "Search by Team Name",
        "Search by Role",
        "Search by Hackathon",
        "Search by Skills",
        "Search by College Name"
    )

    var placeHolderIndex by remember { mutableIntStateOf(0) }

    val vacancies by if (searchText.isNotEmpty()) {
        searchRoleVacancy.searchedVacanciesUiState.collectAsState()
    } else {
        homeScreenViewModel.vacancyUiState.collectAsState()
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            placeHolderIndex = (placeHolderIndex + 1) % searchOption.size
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(stringResource(R.string.app_name), fontWeight = FontWeight.Bold)
            },
            colors = topAppBarColors(
                containerColor = Color(0xFFEFEEFF),
            ),
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.browseback),
                        contentDescription = null,
                        tint = Black
                    )
                }
            }
        )
    }) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = BackgroundGradientColor
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(modifier = Modifier.height(1.dp) , color = Color.LightGray)

            CustomRoundedCorner(stringResource(R.string.find_your_team))


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
                    vacanciesUiState = vacancies,
                    homeScreenViewModel = homeScreenViewModel,
                    saveVacancy = saveVacancy,
                    idsOfSavedVacancy = idsOfSavedVacancy,
                    onVacancyReportBtnClick = {
                        showReportDialog = true
                        vacancyIdToReport = it
                    }
                )
            }
        }
    }
}
@Composable
fun ShowListOfVacancies(
    modifier: Modifier,
    vacanciesUiState: UiState<List<VacancyDetails>>,
    homeScreenViewModel: HomeScreenViewModel,
    saveVacancy: (VacancyDetails) -> Unit,
    idsOfSavedVacancy: List<VacancyEntity>,
    onVacancyReportBtnClick: (String) -> Unit = {}
) {




    when(vacanciesUiState){
        is UiState.Success -> {
            val vacancies = vacanciesUiState.data

            val vacancyIdSet = idsOfSavedVacancy.map { it.vacancyId }

            val filteredVacancy = vacancies.filter { !vacancyIdSet.contains(it.postId) }


            Log.d("VacancyDebugging","Success ")

            LazyColumn(
                modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                items(filteredVacancy) { vacancy ->
                        SingleVacancyCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                            ,
                            vacancy,
                            onSaveVacancy = {
                                saveVacancy(it)
                            }, onReportVacancyBtnClick = {
                                onVacancyReportBtnClick(vacancy.postId)
                            }, isSaved = false
                        )
                }
                item {
                    if(vacancies.isEmpty() || (vacancies.size-idsOfSavedVacancy.size)==0) {
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
        is UiState.Error -> {
            Log.d("VacancyDebugging","Error ")

            Box(contentAlignment = Alignment.Center) {
                LoadAnimation(
                    modifier = Modifier.size(200.dp),
                    animation = R.raw.noresult,
                    playAnimation = true
                )
            }
        }
        is UiState.Loading -> {
            Log.d("VacancyDebugging","Loading ")

            Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
                CircularProgressIndicator(modifier = Modifier.size(36.dp), color = Color.Black)
            }
        }
        is UiState.Idle -> {
            Log.d("VacancyDebugging","Loading ")

        }
    }


}
