package com.example.new_campus_teamup.roleprofile.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.UiState
import com.example.new_campus_teamup.clean_code.PostType
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.helper.ReportPostDialog
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.helper.show
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.room.RoleEntity
import com.example.new_campus_teamup.screens.homescreens.CustomRoundedCorner
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.RoleCardSurfaceVariant
import com.example.new_campus_teamup.ui.theme.RoleOnCardSurfaceVariant
import com.example.new_campus_teamup.viewmodels.HomeScreenViewModel
import com.example.new_campus_teamup.viewmodels.SearchRoleVacancy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RolesScreen(
    homeScreenViewModel: HomeScreenViewModel,
    searchRoleVacancy: SearchRoleVacancy,
    navController: NavController,
) {


    var isFocused by remember { mutableStateOf(false) }
    val searchText by searchRoleVacancy.searchRoleText.collectAsStateWithLifecycle()

    val idOfSavedRoles by homeScreenViewModel.idsOfSavedRoles.collectAsStateWithLifecycle()

//    Log.d("RoleScreen","Saved id from room ${idOfSavedRoles.size}")


    var postId by remember { mutableStateOf<String?>(null) }
    val reportPostUiState by homeScreenViewModel.reportPostUiState.collectAsStateWithLifecycle()
    var showReportDialog by remember { mutableStateOf(false) }

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
            homeScreenViewModel.reportPost(PostType.ROLE , postId !!)
            postId = null
        })

    LaunchedEffect(Unit) {
        homeScreenViewModel.observeRolesInRealTime()
    }





    val roles by if (searchText.isNotEmpty()) {
        searchRoleVacancy.searchedRolesUiState.collectAsState()
    } else {
        homeScreenViewModel.rolesUiState.collectAsState()
    }



    val placeholders = listOf("Search by Role", "Search by Name", "Search by College Name")
    var currentPlaceholderIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(2000)
            currentPlaceholderIndex = (currentPlaceholderIndex + 1) % placeholders.size
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
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = BackgroundGradientColor
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HorizontalDivider(modifier = Modifier.height(1.dp), color = Color.LightGray)

            CustomRoundedCorner(stringResource(R.string.find_team_members))
            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchQuery ->
                        searchRoleVacancy.onSearchRoleTextChange(searchQuery)
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
                                text = placeholders[currentPlaceholderIndex],
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

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            ) {
                ShowListOfRoles(
                    modifier = Modifier.fillMaxSize(),
                    rolesUiState = roles,
                    saveRole = {
                        homeScreenViewModel.saveRole(roleDetails = it)
                    },
                    idOfSavedRoles = idOfSavedRoles,
                    onReportRoleBtnClick = {
                        showReportDialog = true
                        postId = it
                    }
                )
            }
        }
    }
}

@Composable
fun ShowListOfRoles(
    modifier: Modifier,
    rolesUiState: UiState<List<RoleDetails>>,
    saveRole: (RoleDetails) -> Unit,
    idOfSavedRoles: List<RoleEntity>,
    onReportRoleBtnClick: (String) -> Unit = {}
) {



    when (rolesUiState) {
        is UiState.Success -> {

            val savedRoleIdsSet = idOfSavedRoles.map { it.roleId }.toSet()

            val filteredRoles = rolesUiState.data.filter { !savedRoleIdsSet.contains(it.postId) }

            LazyColumn(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if(filteredRoles.isEmpty() || rolesUiState.data.size - idOfSavedRoles.size == 0){
                    item {
                        Box(contentAlignment = Alignment.Center) {
                            LoadAnimation(
                                modifier = Modifier.size(200.dp),
                                animation = R.raw.noresult,
                                playAnimation = true
                            )
                        }
                    }
                }
                else {
                    items(filteredRoles) { role ->
                        Log.d("FetchedRole", "Showing unsaved role")
                        SingleRoleCard(role, onSaveRoleClicked = {
                            saveRole(it)
                        }, onReportRoleBtnClick = {
                            onReportRoleBtnClick(role.postId)
                        }, isSaved = false)
                    }
                }

            }
        }

        is UiState.Error -> {
            Box(contentAlignment = Alignment.Center) {
                LoadAnimation(
                    modifier = Modifier.size(200.dp),
                    animation = R.raw.noresult,
                    playAnimation = true
                )
            }
        }

        is UiState.Loading -> {
            Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
                CircularProgressIndicator(modifier = Modifier.size(36.dp), color = Color.Black)
            }
        }

        is UiState.Idle -> {

        }
    }
}



