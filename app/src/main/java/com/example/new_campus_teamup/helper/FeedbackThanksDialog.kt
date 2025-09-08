package com.example.new_campus_teamup.helper

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.White

@Composable
fun FeedbackThanksDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = onDismiss,
               ) {
                Text("OK" , color = Color.Black)
            }
        },
        title = {
            Text("Thank you!", style = MaterialTheme.typography.titleLarge,  color = Color.Black)
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    LoadAnimation(modifier = Modifier.background(Color.Transparent), animation = R.raw.feedback, playAnimation = true)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text("We appreciate your feedback!", textAlign = TextAlign.Center , color = Color.Black)
            }
        },
        shape = RoundedCornerShape(16.dp),
         containerColor = Color(0xFFbce3f6) , modifier = Modifier.clip(
            RoundedCornerShape(30.dp)
        )
            .border(1.dp, BorderColor, RoundedCornerShape(30.dp))
    )
}
