package com.example.new_campus_teamup.project.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.helper.ReportPostDialog
import com.example.new_campus_teamup.helper.ShimmerEffect
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.screens.homescreens.CustomRoundedCorner
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.viewmodels.HomeScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    homeScreenViewModel: HomeScreenViewModel,
    navController: NavController,
    saveProject: (ProjectDetails) -> Unit
) {

    val projects = homeScreenViewModel.listOfAllProjects.collectAsStateWithLifecycle()


    val isLoading by homeScreenViewModel.isProjectLoading.collectAsStateWithLifecycle()

    val listOfSavedProjects by homeScreenViewModel.listOfSavedProjects.collectAsStateWithLifecycle()

    Log.d("ProjectScreen", "Saved project id size is ${listOfSavedProjects.size}")

    val context = LocalContext.current

    val projectListState = rememberLazyListState()

    var postIdToReport by remember { mutableStateOf<String?>(null) }
    val reportPostUiState by homeScreenViewModel.reportPostUiState.collectAsStateWithLifecycle()
    var showReportDialog by remember { mutableStateOf(false) }


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
        homeScreenViewModel.reportPost("projects" , postIdToReport!!)
        postIdToReport = null
    })

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

        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        BackgroundGradientColor
                    )
                )
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            HorizontalDivider(modifier = Modifier.height(1.dp) , color = Color.LightGray)


            LazyColumn(
                state = projectListState,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    CustomRoundedCorner(stringResource(R.string.collaborate_on_projects))
                    Spacer(modifier = Modifier.height(10.dp))
                }
                items(projects.value) { project ->
                    ShimmerEffect(
                        modifier = Modifier.padding(4.dp),
                        isLoading = isLoading,
                        contentAfterLoading = {

                            val savedProjectIdSet = listOfSavedProjects.map { it.projectId }

                            // this means user saved this and no need to show this project again
                            if (!savedProjectIdSet.contains(project.projectId)) {
                                Log.d("ProjectId", "Showing Project")
                                SingleProject(project, onSaveProjectClicked = { projectId ->
                                    saveProject(project)
                                }, onReportProjectBtnClick = {
                                    showReportDialog= true
                                    postIdToReport = project.projectId
                                }, isSaved = false)
                            }

                        })
                }

                item {

                    if (projects.value.isEmpty() || (projects.value.size - listOfSavedProjects.size) == 0) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
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

    }
}