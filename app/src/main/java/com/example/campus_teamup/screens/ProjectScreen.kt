package com.example.campus_teamup.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_teamup.helper.ShimmerEffect
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.viewmodels.HomeScreenViewModel
import dagger.Lazy


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(homeScreenViewModel: HomeScreenViewModel) {

    val projects = homeScreenViewModel.projectFlow.collectAsState()

    val isRefreshing by homeScreenViewModel.isProjectRefreshing.collectAsState()
    val isLoading by homeScreenViewModel.isProjectLoading.collectAsState()



    Log.d("Project","$isRefreshing in Project screen")
    Box(modifier = Modifier
        .background(BackGroundColor)
        .fillMaxSize()){

        PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = {
            Log.d("Project","going to refresh projects")
           homeScreenViewModel.observeProjectInRealTime()
        }) {

            LazyColumn(){
                items(projects.value){project->
                    ShimmerEffect(modifier = Modifier.padding(1.dp), isLoading = isLoading , contentAfterLoading = {
                        SingleProject(project)
                    })
                }
            }
        }
    }


}