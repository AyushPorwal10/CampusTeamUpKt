package com.example.new_campus_teamup.yourposts


import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightBlueColor
import com.example.new_campus_teamup.ui.theme.TopAppBarColor
import com.example.new_campus_teamup.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourPost(yourPostViewModel: YourPostViewModel) {
    val bgColor = BackGroundColor
    val textColor = White
    val activity = LocalContext.current as? Activity

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    LaunchedEffect(Unit) {
        yourPostViewModel.errorMessage.collect { error ->
            error?.let {
                ToastHelper.showToast(context, error)
                yourPostViewModel.clearError()
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.your_posts) , fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TopAppBarColor,
                    titleContentColor = Black,
                    navigationIconContentColor = Black
                ),
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.browseback),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
            )
        },
        content = { paddingValues ->


            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = BackgroundGradientColor
                        )
                    )
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BorderColor)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = {
                            navController.navigate("postedRoles") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentDestination == "postedRoles") LightBlueColor else Color.LightGray
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.roles),
                            color = if (currentDestination == "postedRoles")  White else Black
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            navController.navigate("postedVacancy") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentDestination == "postedVacancy") LightBlueColor else Color.LightGray
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.vacancies),
                            color = if (currentDestination == "postedVacancy")  White else Black
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            navController.navigate("postedProjects") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentDestination == "postedProjects") LightBlueColor else Color.LightGray
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.projects),
                            color = if (currentDestination == "postedProjects")  White else Black
                        )
                    }
                }

                Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    NavHost(navController, startDestination = "postedRoles") {
                        composable("postedRoles") {
                            ShowPostedRoles(yourPostViewModel)
                        }
                        composable("postedVacancy") {
                            ShowPostedVacancy(yourPostViewModel)
                        }
                        composable("postedProjects") {
                            ShowPostedProjects(yourPostViewModel)
                        }
                    }
                }
            }
        }
    )
}
