package com.example.new_campus_teamup.screens.profilescreens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.UiState
import com.example.new_campus_teamup.email_pass_login.LoginSignUp
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.helper.show
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

    var showAccountDeletionDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current


    LaunchedEffect(Unit) {

    }
    val deleteUserAccountUiState by userProfileViewModel.deleteUserAccountUiState.collectAsState()

    when(val state = deleteUserAccountUiState){
        is UiState.Success -> {

            userProfileViewModel.logoutUser {
                val navigateToLogin = Intent(context, LoginSignUp::class.java)
                navigateToLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                context.startActivity(navigateToLogin)
            }

        }
        is UiState.Error -> {
            val errorMessage = state.errorMessage
            ToastHelper.showToast(context , errorMessage)
            userProfileViewModel.clearDeleteAccountUiState()
        }
        else -> {}
    }



    if (showAccountDeletionDialog) {
        DeleteAccountDialog(deleteUserAccountUiState is UiState.Loading, onDismiss = {
            showAccountDeletionDialog = false
        }) {password ->
            userProfileViewModel.deleteUserAccount()
        }
    }


    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(stringResource(R.string.app_name))
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFEDF9FE)
            ),
            actions = {
                Icon(
                    painter = painterResource(R.drawable.delete_account),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(6.dp)
                        .size(24.dp)
                        .clickable {
                            showAccountDeletionDialog = true
                        }
                )
            }
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


                ProfileCard(userProfileViewModel, userName, userEmail)
                EducationDetailsCard(userProfileViewModel)
                SkillsCard(userProfileViewModel)
                CodingProfilesCard(userProfileViewModel)
            }
        }
    }
}
