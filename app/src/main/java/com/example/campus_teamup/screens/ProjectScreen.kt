package com.example.campus_teamup.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ProjectsScreen() {

    Box(modifier =  Modifier.fillMaxSize()){

        Column(modifier = Modifier.fillMaxSize().
        align(Alignment.Center),
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Projects Dalo Open Source Karo")
        }
    }
}