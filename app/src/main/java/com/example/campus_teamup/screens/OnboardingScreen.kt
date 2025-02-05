package com.example.campus_teamup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White

@Composable
fun OnboardingScreen(navigateToLoginSignUpScreen : () -> Unit ={} ) {

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(BackGroundColor)) {

        val (cloudMessageBox , appNameBox , imageOfThinkingBox , discoverTalentBtn) = createRefs()


//        Box(modifier = Modifier
//            .constrainAs(cloudMessageBox) {
//                top.linkTo(parent.top, margin = 30.dp)
//                start.linkTo(parent.start)
//                end.linkTo(parent.end)
//            }
//            .fillMaxWidth(0.6f) ,
//            contentAlignment = Alignment.Center){
//            Image(painter = painterResource(id = R.drawable.cloudwithmessagepng),
//                contentDescription = "")
//        }
//
//        Box(modifier = Modifier
//            .constrainAs(appNameBox) {
//                top.linkTo(cloudMessageBox.bottom)
//                start.linkTo(parent.start)
//            }
//            .fillMaxWidth(0.6f)
//            .fillMaxHeight(0.5f)){
//            Image(painter = painterResource(id = R.drawable.hasucovered),
//                contentDescription = "",
//                modifier = Modifier.fillMaxSize() , contentScale = ContentScale.Crop)
//        }
//
//        Box(modifier = Modifier
//            .constrainAs(imageOfThinkingBox) {
//                top.linkTo(cloudMessageBox.bottom)
//                start.linkTo(appNameBox.end)
//                end.linkTo(parent.end)
//            }
//            .fillMaxWidth(0.4f)
//            .fillMaxHeight(0.5f)){
//            Image(painter = painterResource(id = R.drawable.thinking_person),
//                contentDescription = "",
//                modifier = Modifier.fillMaxSize() , contentScale = ContentScale.Crop)
//        }
        Image(painter = painterResource(id = R.drawable.onboarding),
            contentDescription = "onboardingscreen",
            modifier = Modifier.fillMaxWidth(1f).fillMaxHeight(0.8f).constrainAs(imageOfThinkingBox){
                top.linkTo(parent.top , margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        OutlinedButton(
            onClick = { navigateToLoginSignUpScreen()},
            modifier = Modifier.constrainAs(discoverTalentBtn){
                top.linkTo(imageOfThinkingBox.bottom , margin = 15.dp)
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