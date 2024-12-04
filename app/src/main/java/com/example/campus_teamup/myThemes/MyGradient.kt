package com.example.campus_teamup.myThemes

import androidx.compose.ui.graphics.Brush
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.LightWhite
import com.example.campus_teamup.ui.theme.White

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