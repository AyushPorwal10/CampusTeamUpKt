package com.example.campus_teamup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.ui.theme.BackGroundColor


@Preview
@Composable
fun ProjectsScreen() {

    Box(modifier = Modifier.background(BackGroundColor).fillMaxSize()){
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)){
            items(5){
                SingleProject()
            }
        }
    }

}