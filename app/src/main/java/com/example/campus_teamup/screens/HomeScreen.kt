package com.example.campus_teamup.screens

import android.content.Intent
import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.PrimaryBlack
import com.example.campus_teamup.myThemes.PrimaryWhiteGradient
import com.example.campus_teamup.myactivities.DrawerItemActivity
import com.example.campus_teamup.mysealedClass.BottomNavScreens
import com.example.campus_teamup.mysealedClass.DrawerItemScreens
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.BluePrimary
import com.example.campus_teamup.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen() {
    // Extract colors based on theme
    val bgColor = if (isSystemInDarkTheme()) Black else White
    val textColor = if (isSystemInDarkTheme()) White else Black
    val navController  = rememberNavController()

    val context = LocalContext.current

    val selected = remember { mutableIntStateOf(R.drawable.vacancies) }

    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                modifier =  Modifier.heightIn(600.dp)
            ) {
                // Drawer header with user info (can be customized further)
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clickable {
                        val intent = Intent(  context , DrawerItemActivity::class.java)
                        intent.putExtra("DrawerItem" , "userProfile")
                        context.startActivity(intent)
                    }) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(painter = painterResource(id = R.drawable.profile), contentDescription = null)

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Ayush Porwal" , style = MaterialTheme.typography.titleMedium)
                            Text(text = "ayush@gmail.com" , style = MaterialTheme.typography.titleSmall)
                        }
                    }

                }
                HorizontalDivider()

                // Notifications menu item
                NavigationDrawerItem(
                    label = { Text(text = "Notifications") },
                    selected = false,
                    icon = {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = textColor
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }

                        val intent = Intent(  context , DrawerItemActivity::class.java)
                        intent.putExtra("DrawerItem" , "notifications")
                        context.startActivity(intent)

                    }
                )

                // Team Details menu item
                NavigationDrawerItem(
                    label = { Text(text = "Team Details") },
                    selected = false,
                    icon = {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = textColor
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        val intent = Intent(  context , DrawerItemActivity::class.java)
                        intent.putExtra("DrawerItem" , "teamDetails")
                        context.startActivity(intent)

                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Campus TeamUp", color = textColor) },
                    colors = topAppBarColors(
                        containerColor = bgColor,
                        titleContentColor = textColor,
                        navigationIconContentColor = textColor
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = null,
                                tint = textColor
                            )
                        }
                    }
                )
            },
            bottomBar = {
                        BottomAppBar(
                            containerColor = bgColor,
                        ) {
                            IconButton(onClick = {
                                selected.intValue = R.drawable.vacancies
                                navController.navigate(BottomNavScreens.Vacancies.screen){
                                    popUpTo(0)
                                }
                            },
                                modifier = Modifier.weight(1f))
                            {
                                Column {
                                    Icon(painter = painterResource(id = R.drawable.vacancies) , contentDescription = null ,modifier = Modifier.size(26.dp)
                                        , tint = if(selected.intValue == R.drawable.vacancies ) BluePrimary else Color.Gray)
                                    Text(text = stringResource(id = R.string.vacancies) , style = MaterialTheme.typography.titleSmall, color = textColor)

                                }

                            }
                            IconButton(onClick = {
                                selected.intValue = R.drawable.roles
                                navController.navigate(BottomNavScreens.Roles.screen){
                                    popUpTo(0)
                                }
                            },
                                modifier = Modifier.weight(1f))
                            {
                                Column {
                                    Icon(painter = painterResource(id = R.drawable.roles) , contentDescription = null ,modifier = Modifier.size(26.dp)
                                        , tint = if(selected.intValue == R.drawable.roles ) BluePrimary else Color.Gray)
                                    Text(text = stringResource(id = R.string.roles) , style = MaterialTheme.typography.titleSmall, color = textColor)
                                }

                            }

                            IconButton(onClick = {
                                selected.intValue = R.drawable.projects
                                navController.navigate(BottomNavScreens.Projects.screen){
                                    popUpTo(0)
                                }
                            },
                                modifier = Modifier.weight(1f))
                            {
                                Column {
                                    Icon(painter = painterResource(id = R.drawable.projects) , contentDescription = null ,modifier = Modifier.size(26.dp)
                                        , tint = if(selected.intValue == R.drawable.projects ) BluePrimary else Color.Gray)
                                    Text(text = stringResource(id = R.string.projects) , style = MaterialTheme.typography.titleSmall, color = textColor)
                                }


                            }

                        }
            },
            content = { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = BottomNavScreens.Roles.screen,
                    modifier = Modifier.padding(paddingValues)
                ) {

                    // bottom nav items
                    composable(BottomNavScreens.Roles.screen) { RolesScreen() }
                    composable(BottomNavScreens.Projects.screen) { ProjectsScreen() }
                    composable(BottomNavScreens.Vacancies.screen) { VacanciesScreen() }
                }

            }
        )
    }
}


