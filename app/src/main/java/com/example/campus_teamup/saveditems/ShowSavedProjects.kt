package com.example.campus_teamup.saveditems

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.LoadAnimation
import com.example.campus_teamup.mydataclass.ProjectDetails
import com.example.campus_teamup.project.screens.SingleProject
import com.example.campus_teamup.ui.theme.BackGroundColor

@Composable
fun ShowSavedProjects(
    savedProjectList: State<List<ProjectDetails>>,
    onProjectUnsave: (String) -> Unit,
) {
    Log.d("FetchingProjects", "In lazy it is ${savedProjectList.value.size}")
    LazyColumn(modifier = Modifier.background(BackGroundColor)) {
        items(savedProjectList.value) {
            Log.d("FetchingProjects", "Single Project loads")
            SingleProject(it, onSaveProjectClicked = {
                // this is to unsave projects
                onProjectUnsave(it)
            }, true)
        }

        item {
            if(savedProjectList.value.isEmpty()){
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