package com.example.new_campus_teamup.myAnimation

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun FloatingBubbles() {
    val infiniteTransition = rememberInfiniteTransition(label = "floating")

    val offsetY1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY1"
    )


    val offsetY2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY2"
    )

    // Floating circles
    Box(
        modifier = Modifier
            .offset(x = 50.dp, y = 250.dp + offsetY1.dp)
            .size(80.dp)
            .clip(CircleShape)
            .background(Color(0xFFECECEC))
            .blur(1.dp)
    )

    Box(
        modifier = Modifier
            .offset(x = 100.dp, y = 800.dp+offsetY2.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0xFFEDF9FE))
            .blur(2.dp)
    )

    Box(
        modifier = Modifier
            .offset(x = 360.dp, y = 700.dp + offsetY1.dp)
            .size(20.dp)
            .clip(CircleShape)
            .background(Color(0xFFEDF9FE))
            .blur(1.dp)
    )
}
