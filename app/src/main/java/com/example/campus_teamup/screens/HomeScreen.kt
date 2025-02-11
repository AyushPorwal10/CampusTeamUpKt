package com.example.campus_teamup.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campus_teamup.R
import com.example.campus_teamup.myactivities.CreatePost
import com.example.campus_teamup.myactivities.DrawerItemActivity
import com.example.campus_teamup.mysealedClass.BottomNavScreens
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(context: Context = LocalContext.current) {

    val bgColor = BackGroundColor
    val textColor = White

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var multifloatingState by remember {
        mutableStateOf(MultifloatingState.Collapse)
    }
    val items = listOf(
        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.vacancies),
            label = "Vacancy",
            identifier = Identifier.Role.name
        ),
        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.projects),
            label = "Project",
            identifier = Identifier.Vacancy.name
        ),
        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.roles),
            label = "Role",
            identifier = Identifier.Project.name
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
                            val intent = Intent(context, DrawerItemActivity::class.java)
                            intent.putExtra("DrawerItem", "userProfile")
                            context.startActivity(intent)
                        },
                    contentAlignment = Alignment.CenterStart
                ) {

                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (userImage, userName, userEmail) = createRefs()

                        Image(
                            painterResource(id = R.drawable.profile),
                            contentDescription = stringResource(id = R.string.user_profile_icon),
                            modifier = Modifier
                                .constrainAs(userImage) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                }
                                .size(50.dp)
                        )

                        Text(
                            text = "Ayush Porwal",
                            modifier = Modifier.constrainAs(userName) {
                                start.linkTo(userImage.start)
                                top.linkTo(userImage.bottom, margin = 10.dp)

                            },
                            color = White
                        )

                        Text(
                            text = "ayushporwal1010@gmail.com",
                            modifier = Modifier.constrainAs(userEmail) {
                                start.linkTo(userName.start) // Align start with userName
                                top.linkTo(userName.bottom, margin = 2.dp)
                            },
                            color = LightTextColor
                        )
                    }
                }

                HorizontalDivider()

                // Notifications menu item

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

                // Team Details menu item

                NavItem(
                    coroutineScope,
                    drawerState,
                    R.drawable.vacancies,
                    stringResource(id = R.string.teamDetails)
                ) {
                    val intent = Intent(context, DrawerItemActivity::class.java)
                    intent.putExtra("DrawerItem", "teamDetails")
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

                // logout button

                LogOutButton(coroutineScope, drawerState)
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
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
                    containerColor = bgColor,
                ) {
                    HandlingBottomAppBar(selected, navController, Modifier.weight(1f))
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
fun LogOutButton(coroutineScope: CoroutineScope, drawerState: DrawerState) {

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
            })
    }

}




