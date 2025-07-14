package com.example.new_campus_teamup.screens.profilescreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R


@Composable
fun DetailsCardHeading(showEditButton : Boolean , onEditButtonClick : () -> Unit , heading : String ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            heading,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        if(showEditButton){
            Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.clickable {
                onEditButtonClick()
            })
        }

    }
}