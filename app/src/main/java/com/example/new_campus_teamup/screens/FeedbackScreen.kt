package com.example.new_campus_teamup.screens

import android.app.Activity
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.FeedbackThanksDialog
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.helper.ProgressIndicator
import com.example.new_campus_teamup.myAnimation.FloatingBubbles
import com.example.new_campus_teamup.myThemes.TextFieldStyle

import com.example.new_campus_teamup.mydataclass.FeedbackData
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.ButtonColor
import com.example.new_campus_teamup.ui.theme.RoleCardGradient
import com.example.new_campus_teamup.ui.theme.TopAppBarColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.UserDataSharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(onSubmit: (FeedbackData) -> Unit , userDataSharedViewModel: UserDataSharedViewModel) {
    var rating by remember { mutableStateOf(0) }
    var feedbackText by remember { mutableStateOf("") }
    var recommend by remember { mutableStateOf<Boolean?>(null) }
    val isFeedbackSent = userDataSharedViewModel.isFeedbackSent.collectAsState()
    val context = LocalContext.current
    val activity = LocalContext.current as? Activity
    val isFeedbackSending = userDataSharedViewModel.isFeedbackSending.collectAsState()


    if(isFeedbackSent.value){
        FeedbackThanksDialog(onDismiss = {
            rating = 0
            recommend = null
            feedbackText = ""
         userDataSharedViewModel.updateFeedbackSentStatus()
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.feedback),
                        color = Black,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TopAppBarColor,
                    navigationIconContentColor = Black
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        activity?.finish()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.browseback),
                            contentDescription = null,
                            tint = Black
                        )
                    }
                }
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = BackgroundGradientColor
                    )
                )
                .padding(30.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.size(60.dp))

            Box(modifier = Modifier.size(200.dp)){
                LoadAnimation(modifier = Modifier ,animation = R.raw.feedback, playAnimation = true)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "We value your feedback!",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Black
            )
            Text(
                "Help us make Campus TeamUp better by sharing your experience",
                textAlign = TextAlign.Center,
                color = Black,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFbce3f6))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("How would you rate your experience?", color = Black)

                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        repeat(5) { index ->
                            Icon(
                                painter = painterResource(if (index < rating) R.drawable.filledstar else R.drawable.star),
                                contentDescription = "Star ${index + 1}",
                                tint = Color(0xFF4D00E7),
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable { rating = index + 1 }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Your feedback", color = Black , modifier = Modifier.padding(4.dp) , fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = feedbackText,
                        colors = TextFieldStyle.myTextFieldColor(),
                        onValueChange = { feedbackText = it },
                        placeholder = { Text("Tell us what you think...", color = Black) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Would you recommend Campus TeamUp to your friends?", color = Black)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = recommend == true,
                            onClick = { recommend = true },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF4D00E7),
                                unselectedColor = Color.Black
                            )
                        )
                        Text("Yes", color = Black)
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = recommend == false,
                            onClick = { recommend = false },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.Red,
                                unselectedColor = Color.Black
                            )
                        )
                        Text("No", color = Black)
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(RoleCardGradient , RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp),
                ) {
                    Text("Submit Feedback" , color = White , fontWeight = FontWeight.SemiBold, )
                }
            }
        }
    }

}
