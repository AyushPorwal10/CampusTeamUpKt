package com.example.new_campus_teamup.screens.profilescreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.myAnimation.FloatingBubbles
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.viewmodels.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userProfileViewModel: UserProfileViewModel,
    userName: String,
    userEmail: String
) {

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(stringResource(R.string.app_name))
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFEDF9FE)
            )
        )
    }) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = BackgroundGradientColor,
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        ) {
            FloatingBubbles()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                ProfileCard(userProfileViewModel, userName , userEmail)
                EducationDetailsCard(userProfileViewModel)
                SkillsCard(userProfileViewModel)
                CodingProfilesCard(userProfileViewModel)
            }
        }
    }
}
