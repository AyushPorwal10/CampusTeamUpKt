package com.example.campus_teamup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.R
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.White


@Preview
@Composable
fun SingleRole() {
    val textColor = if(isSystemInDarkTheme()) White else Black
    Box(
        modifier = Modifier.fillMaxSize()
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, color = Black, shape = RoundedCornerShape(22.dp))
    ){
        Column(horizontalAlignment = Alignment.Start , modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center)
        ) {


            Row(verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Start){
                
                Image(painter = painterResource(id = R.drawable.profile), contentDescription = null , modifier = Modifier.size(30.dp))
                
                Text(text = "Team Name" , style = MaterialTheme.typography.titleLarge , color = textColor)
            }

            Text(text = "Looking for " , style = MaterialTheme.typography.titleMedium , color = textColor)

            Row(verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center , modifier = Modifier.fillMaxWidth()) {
                Text(text = "Know More" , style =  MaterialTheme.typography.titleSmall , color = textColor)
                Icon(painter = painterResource(id = R.drawable.filter), contentDescription = null)
            }
        }
    }
}