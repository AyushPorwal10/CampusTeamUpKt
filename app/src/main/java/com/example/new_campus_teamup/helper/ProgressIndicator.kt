package com.example.new_campus_teamup.helper

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.White

object ProgressIndicator{
    @Composable
    fun showProgressBar(modifier: Modifier , canShow : Boolean) {
        if(canShow){
            show(modifier)
        }
    }

}
@Composable
fun show(modifier : Modifier){
    CircularProgressIndicator(
        modifier = modifier.size(40.dp),
        color = White,
        trackColor = BackGroundColor,
    )
}