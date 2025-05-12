package com.example.new_campus_teamup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.FeedbackThanksDialog
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.helper.ProgressIndicator
import com.example.new_campus_teamup.myThemes.TextFieldStyle

import com.example.new_campus_teamup.mydataclass.FeedbackData
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.UserDataSharedViewModel

@Composable
fun FeedbackScreen(onSubmit: (FeedbackData) -> Unit , userDataSharedViewModel: UserDataSharedViewModel) {
    var rating by remember { mutableStateOf(0) }
    var feedbackText by remember { mutableStateOf("") }
    var recommend by remember { mutableStateOf<Boolean?>(null) }
    val isFeedbackSent = userDataSharedViewModel.isFeedbackSent.collectAsState()
    val context = LocalContext.current
    
    val isFeedbackSending = userDataSharedViewModel.isFeedbackSending.collectAsState()


    if(isFeedbackSent.value){
        FeedbackThanksDialog(onDismiss = {
         userDataSharedViewModel.updateFeedbackSentStatus()
        })
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo placeholder



        Box(modifier = Modifier.size(200.dp)){
            LoadAnimation(modifier = Modifier ,animation = R.raw.feedback, playAnimation = true)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "We value your feedback!",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = White
        )
        Text(
            "Help us make Campus TeamUp better by sharing your experience",
            textAlign = TextAlign.Center,
            color = White,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = BorderColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("How would you rate your experience?", color = White)

                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    repeat(5) { index ->
                        Icon(
                            painter = painterResource(if (index < rating) R.drawable.filledstar else R.drawable.star),
                            contentDescription = "Star ${index + 1}",
                            tint = Color(0xFFFFC107), // Amber
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { rating = index + 1 }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Your feedback", color = White , modifier = Modifier.padding(4.dp))
                OutlinedTextField(
                    value = feedbackText,
                    colors = TextFieldStyle.myTextFieldColor(),
                    onValueChange = { feedbackText = it },
                    placeholder = { Text("Tell us what you think...", color = White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, White, RoundedCornerShape(20.dp))
                        .height(120.dp),
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Would you recommend Campus TeamUp to your friends?", color = White)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = recommend == true,
                        onClick = { recommend = true },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Blue,
                            unselectedColor = Color.White
                        )
                    )
                    Text("Yes", color = White)
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = recommend == false,
                        onClick = { recommend = false },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Blue,
                            unselectedColor = Color.White
                        )
                    )
                    Text("No", color = White)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        if(isFeedbackSending.value){
            ProgressIndicator.showProgressBar(modifier = Modifier, canShow  = isFeedbackSending.value)
        }
        else{
        Button(
            onClick = {
                val feedback = FeedbackData(rating, feedbackText.trim(), recommend)
                onSubmit(feedback)
            },
            enabled = feedbackText.isNotEmpty() && recommend != null && rating > 0,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderColor, RoundedCornerShape(22.dp))
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text("Submit Feedback")
        }
        }
    }
}
