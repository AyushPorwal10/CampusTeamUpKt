package com.example.new_campus_teamup.helper

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.White
import kotlinx.coroutines.delay


@Composable
fun StartChatDialog(onConfirm : () -> Unit ) {
    AlertDialog(
        onDismissRequest = {
        },
        icon = {
            AnimatedIcon()
        },
        containerColor = Color.White,
        confirmButton = {
            Button(onClick = {
                onConfirm()
            } , colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4D00E7)
            )) {
                Text(text = "Start Chat" , color = White , style = MaterialTheme.typography.titleSmall)
            }
        },
        text = {
            Text(text = stringResource(id = R.string.start_chat_message) ,  color = Black , style = MaterialTheme.typography.titleMedium)
        } ,
    )
}


@Composable
private fun AnimatedIcon() {
    var isRotated by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        isRotated = true
    }

    val rotation by animateFloatAsState(
        targetValue = if (isRotated) 360f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ), label = "rotation"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isRotated) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ), label = "iconScale"
    )

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Color(0xFFFF4444).copy(alpha = 0.1f)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.chats),
            contentDescription = "Send Request",
            modifier = Modifier
                .size(32.dp)
                .scale(iconScale)
                .graphicsLayer(rotationZ = rotation),
            tint = MaterialTheme.colorScheme.error
        )
    }
}
