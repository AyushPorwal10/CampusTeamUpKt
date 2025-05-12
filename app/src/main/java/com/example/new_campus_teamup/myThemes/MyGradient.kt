package com.example.new_campus_teamup.myThemes

import androidx.compose.ui.graphics.Brush
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.LightWhite
import com.example.new_campus_teamup.ui.theme.White

val PrimaryWhite = Brush.linearGradient(
    listOf(
        White, White
    )
)

val PrimaryBlack = Brush.linearGradient(
    listOf(
        Black , Black
    )
)

val PrimaryWhiteGradient = Brush.linearGradient(
    listOf(
        White , LightWhite
    )
)