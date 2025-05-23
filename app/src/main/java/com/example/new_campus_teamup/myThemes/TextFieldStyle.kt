package com.example.new_campus_teamup.myThemes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.White


object TextFieldStyle {

    @Composable

    fun myTextFieldColor() = OutlinedTextFieldDefaults.colors(


        cursorColor = White,
        unfocusedPlaceholderColor = LightTextColor,
        focusedPlaceholderColor = LightTextColor,

        unfocusedBorderColor = BorderColor,
        focusedBorderColor = BorderColor,

        unfocusedTextColor = White ,
        focusedTextColor = White,

        focusedLabelColor = White,
        unfocusedLabelColor = LightTextColor


    )
    val defaultShape = RoundedCornerShape(20.dp)

}