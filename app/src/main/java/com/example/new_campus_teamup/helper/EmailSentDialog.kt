package com.example.new_campus_teamup.helper

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.White

@Composable
fun EmailSentDialog( onClick : () -> Unit ) {
    AlertDialog(
        onDismissRequest = {
        },
        confirmButton = {
            Button(onClick = {
               onClick()
            } , colors = ButtonDefaults.buttonColors(
                containerColor = BackGroundColor
            )) {
                Text(text = "Confirm" , color = White , style = MaterialTheme.typography.titleSmall)
            }
        },
        title = {
            Icon(painter = painterResource(R.drawable.email_sent) , contentDescription = null)
        },
        text = {
            Text(text = stringResource(id = R.string.email_sent_message) ,  color = White , style = MaterialTheme.typography.titleMedium)
        } , containerColor = BackGroundColor , modifier = Modifier
            .border(1.dp, BorderColor, RoundedCornerShape(30.dp))
    )
}
