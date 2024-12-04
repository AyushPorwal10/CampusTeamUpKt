package com.example.campus_teamup.myThemes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.LightWhite
import com.example.campus_teamup.ui.theme.White


object TextFieldStyle {

    @Composable

    fun myTextFieldColor() = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = if(isSystemInDarkTheme()) Black else White,

        cursorColor = if(isSystemInDarkTheme()) White else Black,

        unfocusedPlaceholderColor = if(isSystemInDarkTheme()) White else Black,
        focusedPlaceholderColor = if(isSystemInDarkTheme()) White else Black,

        unfocusedBorderColor = if(isSystemInDarkTheme()) LightWhite else Black,
        focusedBorderColor = if(isSystemInDarkTheme()) LightWhite else Black,

        unfocusedTextColor = if(isSystemInDarkTheme()) White else Black ,
        focusedTextColor = if(isSystemInDarkTheme()) White else Black,

        focusedLabelColor = if(isSystemInDarkTheme()) White else Black,
        unfocusedLabelColor = if(isSystemInDarkTheme()) White else Black


    )
    val defaultShape = RoundedCornerShape(24.dp)

}