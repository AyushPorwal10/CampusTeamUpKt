package com.example.new_campus_teamup.screens.homescreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R


@Preview
@Composable
fun CustomRoundedCorner(value: String = "Find your team member.") {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
            .background(
                color = Color(0xFFEFEEFF),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 34.dp,
                    bottomEnd = 34.dp
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEFEEFF)
        ),
        elevation = CardDefaults.cardElevation(
            16.dp
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,) {
            Box(modifier = Modifier.fillMaxWidth() , contentAlignment = Alignment.Center){
                Text(
                    value,
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,

                )
            }

        }
    }
}
