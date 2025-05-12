package com.example.new_campus_teamup.yourposts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.ui.theme.BackGroundColor


@Composable
fun ShowPostedProjects(yourPostViewModel: YourPostViewModel ) {

    val vacancyList = yourPostViewModel.postedProjects.collectAsState()
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackGroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(vacancyList.value){
            YourSingleProject(projectDetails =  it, onProjectDelete = {projectId->
                Log.d("DeleteProject","Project id is $projectId")
                yourPostViewModel.deleteProject(projectId, onDelete = {
                    ToastHelper.showToast(context,"Deleted Successfully")
                }, onError = {
                    ToastHelper.showToast(context,"Something went wrong !")
                })
            })
        }
        item {
            if(vacancyList.value.isEmpty()){
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