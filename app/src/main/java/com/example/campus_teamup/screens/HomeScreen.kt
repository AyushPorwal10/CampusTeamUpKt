package com.example.campus_teamup.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.HandleLogoutDialog
import com.example.campus_teamup.helper.LoadAnimation
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.helper.rememberNetworkStatus
import com.example.campus_teamup.myactivities.DrawerItemActivity
import com.example.campus_teamup.myactivities.UserProfile
import com.example.campus_teamup.myotp.SignUpLogin
import com.example.campus_teamup.mysealedClass.BottomNavScreens
import com.example.campus_teamup.project.screens.ProjectsScreen
import com.example.campus_teamup.roleprofile.screens.RolesScreen
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.vacancy.screens.VacanciesScreen
import com.example.campus_teamup.viewmodels.HomeScreenViewModel
import com.example.campus_teamup.viewmodels.SearchRoleVacancy
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(
    context: Context = LocalContext.current,
    homeScreenViewModel: HomeScreenViewModel,
    searchRoleVacancy: SearchRoleVacancy,
    userId: String?
) {


    val isConnected = rememberNetworkStatus()


    Log.d("Saving", "currentUserId in homescreen is $userId <-")

    val bgColor = BackGroundColor
    val textColor = White
    val userEmailId = remember {
        mutableStateOf("")
    }
    val userData = homeScreenViewModel.userData.collectAsState()
    val userProfileImage = homeScreenViewModel.userImage.collectAsState()

    val bottomAppBarScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    var multifloatingState by remember {
        mutableStateOf(MultifloatingState.Collapse)
    }
    val items = listOf(
        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.vacancies),
            label = "Vacancy",
            identifier = Identifier.Vacancy.name
        ),
        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.projects),
            label = "Project",
            identifier = Identifier.Project.name
        ),
        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.roles),
            label = "Role",
            identifier = Identifier.Role.name
        )

    )
    val navController = rememberNavController()

    val context = LocalContext.current

    val selected = remember { mutableIntStateOf(R.drawable.roles) }

    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                drawerContainerColor = BackGroundColor
            ) {


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .height(150.dp)
                        .clickable {
                            val intent = Intent(context, UserProfile::class.java)
                            context.startActivity(intent)
                        },
                    contentAlignment = Alignment.CenterStart
                ) {

                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (userImage, userNameArea, userEmailArea) = createRefs()


                        AsyncImage(
                            model = userProfileImage.value.ifEmpty { R.drawable.profile },
                            contentDescription = "User Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .border(1.dp, White, CircleShape)
                                .constrainAs(userImage) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                })
                        Text(
                            text = userData.value?.userName ?: "",
                            modifier = Modifier.constrainAs(userNameArea) {
                                start.linkTo(userImage.start)
                                top.linkTo(userImage.bottom, margin = 10.dp)

                            },
                            color = White
                        )

                        Text(
                            text = userData.value?.email ?: "",
                            modifier = Modifier.constrainAs(userEmailArea) {
                                start.linkTo(userNameArea.start) // Align start with userName
                                top.linkTo(userNameArea.bottom, margin = 2.dp)
                            },
                            color = LightTextColor
                        )
                    }
                }

                HorizontalDivider()

                // Notifications menu item

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    NavItem(
                        coroutineScope,
                        drawerState,
                        R.drawable.notifications,
                        stringResource(id = R.string.notifications)
                    ) {
                        val intent = Intent(context, DrawerItemActivity::class.java)
                        intent.putExtra("DrawerItem", "notifications")
                        context.startActivity(intent)
                    }



                    // recents chats

                    NavItem(
                        coroutineScope,
                        drawerState,
                        R.drawable.chats,
                        stringResource(id = R.string.chats)
                    ) {
                        val intent = Intent(context, DrawerItemActivity::class.java)
                        intent.putExtra("DrawerItem", "recentchats")
                        context.startActivity(intent)
                    }


                    NavItem(
                        coroutineScope,
                        drawerState,
                        R.drawable.saveproject,
                        stringResource(id = R.string.your_posts)
                    ) {
                        val intent = Intent(context, DrawerItemActivity::class.java)
                        intent.putExtra("DrawerItem", "yourposts")
                        context.startActivity(intent)
                    }
                    // saved items

                    NavItem(
                        coroutineScope,
                        drawerState,
                        R.drawable.saved_item,
                        stringResource(id = R.string.saved_items)
                    ) {
                        val intent = Intent(context, DrawerItemActivity::class.java)
                        intent.putExtra("DrawerItem", "savedItems")
                        context.startActivity(intent)
                    }

                    // logout button

                    LogOutButton(homeScreenViewModel)

                    NavItem(
                        coroutineScope,
                        drawerState,
                        R.drawable.feedback,
                        stringResource(id = R.string.feedback)
                    ) {
                        val intent = Intent(context, DrawerItemActivity::class.java)
                        intent.putExtra("DrawerItem", "feedback")
                        context.startActivity(intent)
                    }

                }

            }
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
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
            floatingActionButton = {
                MultipleFabButton(
                    multifloatingState = multifloatingState,
                    onMultifloatingStateChange = {
                        multifloatingState = it
                    }, items = items, context = context
                )
            },
            bottomBar = {
                HorizontalDivider(modifier = Modifier.width(2.dp))
                BottomAppBar(
                    scrollBehavior = bottomAppBarScrollBehavior,
                    containerColor = bgColor,
                ) {
                    HandlingBottomAppBar(selected, navController, Modifier.weight(1f))
                }
            }
        ) { paddingValues ->


            LaunchedEffect(isConnected) {
                if (!isConnected) {
                    snackbarHostState.showSnackbar(
                        message = "No Internet Connection",
                        actionLabel = "OK"
                    )
                }
            }


            if(isConnected){
                NavHost(
                    navController = navController,
                    startDestination = BottomNavScreens.Roles.screen,
                    modifier = Modifier.padding(paddingValues)
                ) {

                    composable(BottomNavScreens.Roles.screen) {
                        RolesScreen(homeScreenViewModel, searchRoleVacancy) { roleDetails ->
                            homeScreenViewModel.saveRole(roleDetails, onRoleSaved = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Role Saved Successfully.",
                                        actionLabel = "Ok"
                                    )
                                }
                            }, onError = {
                                Log.e("RoleSaved", "Error is $it")
                                ToastHelper.showToast(context, "Something went wrong !")
                            })
                        }
                    }
                    composable(BottomNavScreens.Projects.screen) {
                        ProjectsScreen(
                            homeScreenViewModel,
                            saveProject = {
                                homeScreenViewModel.saveProject(it, onProjectSaved = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Project Saved Successfully.",
                                            actionLabel = "Ok"
                                        )
                                    }
                                },
                                    onError = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(message = "Something went wrong.")
                                        }
                                    })
                            }
                        )
                    }
                    composable(BottomNavScreens.Vacancies.screen) {
                        VacanciesScreen(
                            homeScreenViewModel, searchRoleVacancy,
                            saveVacancy = {
                                homeScreenViewModel.saveVacancy(it, onVacancySaved = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Vacancy Saved Successfully.",
                                            actionLabel = "Ok"
                                        )
                                    }
                                },
                                    onError = {
                                    })
                            }
                        )
                    }
                }
            }
            else {
                Box(modifier = Modifier.padding(paddingValues).fillMaxSize().background(BackGroundColor) , contentAlignment = Alignment.Center){
                        LoadAnimation(modifier = Modifier.size(200.dp) , animation = R.raw.otp, playAnimation = true)
                }
            }


        }
    }
}

@Composable
fun HandlingBottomAppBar(
    selected: MutableIntState,
    navController: NavController,
    modifier: Modifier
) {
    IconButton(
        onClick = {
            selected.intValue = R.drawable.vacancies
            navController.navigate(BottomNavScreens.Vacancies.screen) {
                popUpTo(0)
            }
        },
        modifier = modifier
    )
    {


        ConstraintLayout {
            val (vacancyBtn, vacancyText) = createRefs()
            Icon(
                painter = painterResource(id = R.drawable.vacancies),
                contentDescription = null,
                tint = if (selected.intValue == R.drawable.vacancies) White else BorderColor,
                modifier = Modifier
                    .size(26.dp)
                    .constrainAs(vacancyBtn) {

                    })

            Text(
                text = stringResource(id = R.string.vacancies),
                style = MaterialTheme.typography.titleSmall,
                color = if (selected.intValue == R.drawable.vacancies) White else BorderColor,
                modifier = Modifier.constrainAs(vacancyText) {

                })

            createVerticalChain(vacancyBtn, vacancyText, chainStyle = ChainStyle.Packed)

        }

    }
    IconButton(
        onClick = {
            selected.intValue = R.drawable.roles
            navController.navigate(BottomNavScreens.Roles.screen) {
                popUpTo(0)
            }
        },
        modifier = modifier
    )
    {
        ConstraintLayout {
            val (roleBtn, roleText) = createRefs()
            Icon(
                painter = painterResource(id = R.drawable.roles),
                contentDescription = null,
                tint = if (selected.intValue == R.drawable.roles) White else BorderColor,
                modifier = Modifier
                    .size(26.dp)
                    .constrainAs(roleBtn) {

                    })

            Text(
                text = stringResource(id = R.string.roles),
                style = MaterialTheme.typography.titleSmall,
                color = if (selected.intValue == R.drawable.roles) White else BorderColor,
                modifier = Modifier.constrainAs(roleText) {

                })

            createVerticalChain(
                roleBtn,
                roleText,
                chainStyle = ChainStyle.Packed
            )
        }

    }

    IconButton(
        onClick = {
            selected.intValue = R.drawable.projects
            navController.navigate(BottomNavScreens.Projects.screen) {
                popUpTo(0)
            }
        },
        modifier = modifier
    )
    {
        ConstraintLayout {
            val (projectBtn, projectText) = createRefs()
            Icon(
                painter = painterResource(id = R.drawable.projects),
                contentDescription = null,
                tint = if (selected.intValue == R.drawable.projects) White else BorderColor,
                modifier = Modifier
                    .size(26.dp)
                    .constrainAs(projectBtn) {

                    })

            Text(
                text = stringResource(id = R.string.projects),
                style = MaterialTheme.typography.titleSmall,
                color = if (selected.intValue == R.drawable.projects) White else BorderColor,
                modifier = Modifier.constrainAs(projectText) {

                })

            createVerticalChain(
                projectBtn,
                projectText,
                chainStyle = ChainStyle.Packed
            )
        }


    }
}

@Composable
fun LogOutButton(homeScreenViewModel: HomeScreenViewModel) {

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    NavigationDrawerItem(
        label = { Text(text = stringResource(id = R.string.logout), color = White) },
        selected = false,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.logout),
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(26.dp)
            )
        },
        onClick = {
            showDialog = true
        }
    )

    if (showDialog) {
        HandleLogoutDialog(
            onDismiss = {
                showDialog = false
            },
            onConfirm = {
                showDialog = false
                homeScreenViewModel.logoutUser(onLogoutSuccess = {
                    val navigateToLogin = Intent(context, SignUpLogin::class.java)
                    navigateToLogin.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(navigateToLogin)
                })
            })
    }

}




