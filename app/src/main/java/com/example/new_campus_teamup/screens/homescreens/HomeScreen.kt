package com.example.new_campus_teamup.screens.homescreens

import android.app.Notification.Action
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.myactivities.DrawerItemActivity
import com.example.new_campus_teamup.myactivities.UserProfile
import com.example.new_campus_teamup.screens.Identifier
import com.example.new_campus_teamup.screens.MinFabItem
import com.example.new_campus_teamup.screens.MultifloatingState
import com.example.new_campus_teamup.screens.MultipleFabButton
import com.example.new_campus_teamup.screens.NavItem
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.HomeScreenViewModel
import com.example.new_campus_teamup.viewmodels.SearchRoleVacancy
import kotlinx.coroutines.launch
import androidx.core.net.toUri


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel,
    navController: NavController,
    searchRoleVacancy: SearchRoleVacancy,
    userId: String?
) {


    val currentUserImageUrl = homeScreenViewModel.currentUserImage.collectAsState()

    // fetching just one time
    LaunchedEffect(Unit) {
        homeScreenViewModel.observeCurrentUserImage()
    }

    Log.d("Saving", "currentUserId in homescreen is $userId <-")

    val textColor = White

    val userData = homeScreenViewModel.userData.collectAsState()

    // val bottomAppBarScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    var multifloatingState by remember {
        mutableStateOf(MultifloatingState.Collapse)
    }
    val items = listOf(
        MinFabItem(
            label = "Vacancy",
            identifier = Identifier.Vacancy.name
        ),
        MinFabItem(
            label = "Project",
            identifier = Identifier.Project.name
        ),
        MinFabItem(
            label = "Role",
            identifier = Identifier.Role.name
        )

    )

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
                    .fillMaxWidth(0.8f)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = BackgroundGradientColor
                        )
                    ),
                drawerContainerColor = Color.Transparent
            ) {


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .height(150.dp)
                        .clickable {
                            val intent = Intent(context, UserProfile::class.java).apply {
                                putExtra("userName", homeScreenViewModel.userData.value?.userName)
                                putExtra("userEmail", homeScreenViewModel.userData.value?.email)
                            }
                            context.startActivity(intent)
                        },
                    contentAlignment = Alignment.CenterStart
                ) {

                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (userImage, userNameArea, userEmailArea) = createRefs()


                        AsyncImage(
                            model = currentUserImageUrl.value.ifEmpty { R.drawable.profile },
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
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = userData.value?.email ?: "",
                            modifier = Modifier.constrainAs(userEmailArea) {
                                start.linkTo(userNameArea.start)
                                top.linkTo(userNameArea.bottom, margin = 2.dp)
                            },
                            color = LightTextColor
                        )
                    }
                }

                HorizontalDivider()

                // Notifications menu item

                Box(modifier = Modifier.fillMaxHeight()) {

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

                        HandleLogoutButton(homeScreenViewModel)

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

                    Box(modifier = Modifier.align(Alignment.BottomCenter)){
                        NavItem(
                            coroutineScope,
                            drawerState,
                            R.drawable.privacy_policy,
                            stringResource(R.string.privacy_policy)
                        ) {
                            val intent = Intent(Intent.ACTION_VIEW,
                                "https://ayushporwal10.github.io/Campus_TeamUp_Privacy_Policy/".toUri())

                            context.startActivity(intent)
                        }
                    }



                }

            }
        }
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Campus TeamUp",
                            color = Black,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = topAppBarColors(
                        containerColor = Color(0xFFEFEEFF),
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
                                tint = Black
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
            }
        ) { paddingValues ->

            HomeScreenContent(
                modifier = Modifier.padding(paddingValues),
                navController,
                userData.value?.userName ?: ""
            )
        }
    }
}


