package com.example.new_campus_teamup.myThemes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.White


object TextFieldStyle {

    @Composable

    fun myTextFieldColor() = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF667eea),
        unfocusedBorderColor = Color(0xFFE2E8F0),
        focusedLabelColor = Color(0xFF667eea),
        cursorColor = Color(0xFF667eea)
    )
    val defaultShape = RoundedCornerShape(20.dp)

}