package com.example.new_campus_teamup.roleprofile.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.NotificationViewModel
import com.example.new_campus_teamup.viewmodels.ViewProfileViewModel


@Composable
fun ViewUserImage(
    viewProfileViewModel: ViewProfileViewModel,
    notificationViewModel: NotificationViewModel,
    receiverName: String?,
    receiverId: String?
) {

    var pressed by remember { mutableStateOf(false) }


        LaunchedEffect(receiverId) {
            if(receiverId != null)
            viewProfileViewModel.observeCurrentUserImage(receiverId)
        }

    val userImage = viewProfileViewModel.userImage.collectAsState()




    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f
    )

    Card(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false

                    }
                )
            }
            .fillMaxWidth(0.9f)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFbce3f6)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier.padding(8.dp),
            ) {

                AsyncImage(
                    model = userImage.value.ifBlank { R.drawable.profile },
                    contentDescription = "User Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(1.dp, White, CircleShape)
                )

                Text(
                    receiverName ?: "",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

            }
        }
    }
}

