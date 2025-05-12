package com.example.new_campus_teamup.project.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.helper.ShimmerEffect
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.viewmodels.HomeScreenViewModel


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


        LazyColumn(state = projectListState , horizontalAlignment = Alignment.CenterHorizontally){
            items(projects.value){project->
                ShimmerEffect(modifier = Modifier.padding(4.dp), isLoading = isLoading , contentAfterLoading = {

                    // this means user saved this and no need to show this project again
                    if(!listOfSavedProjects.value.contains(project.projectId)){
                        Log.d("ProjectId","Showing Project")
                        SingleProject(project , onSaveProjectClicked = {projectId->
                            saveProject(project)
                        } , false)
                    }

                })
            }

            item {

                if(projects.value.isEmpty() || (projects.value.size-listOfSavedProjects.value.size)==0) {
                    Box(modifier = Modifier.fillMaxSize() ,  contentAlignment = Alignment.Center) {
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