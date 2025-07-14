package com.example.new_campus_teamup.saveditems

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.project.screens.SingleProject
import com.example.new_campus_teamup.ui.theme.BackGroundColor

@Composable
fun ShowSavedProjects(
    savedProjectList: State<List<ProjectDetails>>,
    onProjectUnsave: (String) -> Unit,
) {
    Log.d("FetchingProjects", "In lazy it is ${savedProjectList.value.size}")
    LazyColumn(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(savedProjectList.value) {
            Log.d("FetchingProjects", "Single Project loads")
            SingleProject(it, onSaveProjectClicked = {
                // this is to unsave projects
                onProjectUnsave(it)
            }, true)
        }

        item {
            if (savedProjectList.value.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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