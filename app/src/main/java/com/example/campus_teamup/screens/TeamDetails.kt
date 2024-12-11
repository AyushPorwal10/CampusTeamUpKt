package com.example.campus_teamup.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TeamDetailScreen() {
    Box(modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center){

        Text(text = "This is Team Details Section")
    }
}