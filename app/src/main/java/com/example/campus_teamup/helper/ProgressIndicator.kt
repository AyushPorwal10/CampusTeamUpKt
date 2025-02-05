package com.example.campus_teamup.helper

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object ProgressIndicator{
    @Composable
    fun showProgressBar(loading : MutableState<Boolean>) {
        if(loading.value){
            CircularProgressIndicator(
                modifier = Modifier.width(50.dp),
                trackColor = Color.White
            )
        }
    }

}