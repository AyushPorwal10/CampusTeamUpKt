package com.example.new_campus_teamup.roleprofile.screens

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.project.screens.PostProject
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.TopAppBarColor
import com.example.new_campus_teamup.vacancy.screens.PostVacancy
import com.example.new_campus_teamup.viewmodels.CreatePostViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(screenToOpen : String , createPostViewModel: CreatePostViewModel) {



    val activity = LocalContext.current as? Activity
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
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
    }) { paddingValues ->



        Box(
            modifier = Modifier
                .background(brush = Brush.verticalGradient(
                    colors = BackgroundGradientColor
                ))
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {


                when(screenToOpen){
                    "Role" -> PostRole(createPostViewModel)
                    "Vacancy" -> PostVacancy(createPostViewModel)
                    "Project" -> PostProject(createPostViewModel)
                }


        }
    }
}