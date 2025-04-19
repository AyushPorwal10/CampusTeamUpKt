package com.example.campus_teamup.roleprofile.screens

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.LoadAnimation
import com.example.campus_teamup.helper.rememberNetworkStatus
import com.example.campus_teamup.project.screens.PostProject
import com.example.campus_teamup.roleprofile.screens.PostRole
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.vacancy.screens.PostVacancy
import com.example.campus_teamup.viewmodels.CreatePostViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(screenToOpen : String , createPostViewModel: CreatePostViewModel) {

    val isConnected = rememberNetworkStatus()
    val snackbarHostState = remember { SnackbarHostState() }

    val activity = LocalContext.current as? Activity
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = White
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = BackGroundColor,
                navigationIconContentColor = White
            ),
            navigationIcon = {
                IconButton(onClick = {
                    activity?.finish()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.browseback),
                        contentDescription = null,
                        tint = White
                    )
                }
            }
        )
    }) { paddingValues ->

        LaunchedEffect(isConnected) {
            if (!isConnected) {
                snackbarHostState.showSnackbar(
                    message = "No Internet Connection",
                    actionLabel = "OK"
                )
            }
        }


        Box(
            modifier = Modifier
                .background(BackGroundColor)
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {

            if(isConnected){
                when(screenToOpen){
                    "Role" -> PostRole(createPostViewModel)
                    "Vacancy" -> PostVacancy(createPostViewModel)
                    "Project" -> PostProject(createPostViewModel)
                }
            }
            else {
                LoadAnimation(modifier = Modifier.size(200.dp) , animation = R.raw.otp, playAnimation = true)
            }

        }
    }
}