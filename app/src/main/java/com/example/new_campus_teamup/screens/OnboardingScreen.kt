package com.example.new_campus_teamup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.ProgressIndicator
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor

import com.example.new_campus_teamup.ui.theme.White



@Composable
fun OnboardingScreen(
    navigateToLoginSignUpScreen: () -> Unit = {}
) {

    val showProgressBar = remember { mutableStateOf(false) }





    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBCE3F6))
            ) {

        val (progressBar, imageOfThinkingBox, discoverTalentBtn) = createRefs()

        Image(painter = painterResource(id = R.drawable.onboard),
            contentDescription = "onboardingscreen",
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(0.8f)
                .constrainAs(imageOfThinkingBox) {
                    top.linkTo(parent.top, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        if(showProgressBar.value){
            ProgressIndicator.showProgressBar(modifier = Modifier.constrainAs(progressBar){
                top.linkTo(imageOfThinkingBox.bottom, margin = 15.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },showProgressBar.value)
        }
        else {
            OutlinedButton(
                onClick = { navigateToLoginSignUpScreen() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4D00E7)
                ),
                modifier = Modifier.constrainAs(discoverTalentBtn) {
                    top.linkTo(imageOfThinkingBox.bottom, margin = 15.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Text(
                    text = "Discover Talents , Build Team",
                    color = White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
