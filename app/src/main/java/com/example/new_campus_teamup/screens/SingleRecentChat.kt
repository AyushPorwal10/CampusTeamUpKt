package com.example.new_campus_teamup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.new_campus_teamup.helper.Dimensions
import com.example.new_campus_teamup.mydataclass.RecentChats
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.LightWhite
import com.example.new_campus_teamup.ui.theme.White


@Composable
fun SingleRecentChat(chatData : RecentChats, onClick : () -> Unit) {

    Card(onClick = {
        onClick()
    }, elevation = CardDefaults.cardElevation(6.dp), colors = CardDefaults.cardColors(
        containerColor =  Color(0xFF7EEBBE)
    ), modifier = Modifier.fillMaxWidth(0.9f).padding(6.dp)) {

        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(chatData.senderName, fontWeight = FontWeight.Bold , style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(6.dp))

            Text("Created on ${chatData.createdOn}"  , style = MaterialTheme.typography.bodySmall)
        }
    }
}