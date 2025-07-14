package com.example.new_campus_teamup.chatsection.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.mydataclass.SendMessage
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.White

@Composable
fun ShowChats(messageDetails: SendMessage?, currentUserId: String?) {



    Row(
        modifier = Modifier.fillMaxWidth(0.9f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (messageDetails?.senderId == currentUserId) Arrangement.End else Arrangement.Start
    ) {

        Box(modifier = Modifier.clip(RoundedCornerShape(6.dp))
            .background(if (messageDetails?.senderId == currentUserId) Color(0xFFB1FFB6) else Color.LightGray)) {

            Text(
                text = "${messageDetails?.message}",
                modifier = Modifier.padding(6.dp),
                style = MaterialTheme.typography.titleMedium,
                color = Black
            )
        }
    }
}