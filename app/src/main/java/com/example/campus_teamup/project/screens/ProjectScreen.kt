package com.example.campus_teamup.project.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.ShimmerEffect
import com.example.campus_teamup.mydataclass.ProjectDetails
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.viewmodels.HomeScreenViewModel
import com.example.campus_teamup.viewmodels.UserDataSharedViewModel
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(homeScreenViewModel: HomeScreenViewModel , saveProject : (ProjectDetails) -> Unit) {

    val projects = homeScreenViewModel.listOfAllProjects.collectAsState()

    val listOfSavedProjects = homeScreenViewModel.listOfSavedPost.collectAsState()

    val isRefreshing by homeScreenViewModel.isProjectRefreshing.collectAsState()
    val isLoading by homeScreenViewModel.isProjectLoading.collectAsState()


    val projectListState = rememberLazyListState()

    Box(modifier = Modifier
        .background(BackGroundColor)
        .fillMaxSize()){

//        PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = {
//            Log.d("Project","going to refresh projects")
//            // refresh means fetching new projects
//           homeScreenViewModel.refreshProjects()
//        }) {
//
//
//        }

        LaunchedEffect(projectListState){
            snapshotFlow{projectListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index}
                .collectLatest {lastVisibleProject->
                    if(lastVisibleProject != null && lastVisibleProject >= projects.value.size-1){

                        // this will fetch new projects
                        homeScreenViewModel.fetchProjects()
                    }

                }
        }

        LazyColumn(state = projectListState){
            items(projects.value){project->
                ShimmerEffect(modifier = Modifier.padding(1.dp), isLoading = isLoading , contentAfterLoading = {

                    // this means user saved this and no need to show this project again

                    if(!listOfSavedProjects.value.contains(project.projectId)){
                        Log.d("ProjectId","Showing Project")
                        SingleProject(project , onSaveProjectClicked = {projectId->
                            saveProject(project)
                        } , false)
                    }

                })
            }
        }
    }

}