package com.example.campus_teamup.roleprofile.screens

import android.util.Log
import android.widget.Space
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.LoadAnimation
import com.example.campus_teamup.helper.ShimmerEffect
import com.example.campus_teamup.helper.rememberNetworkStatus
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.HomeScreenViewModel
import com.example.campus_teamup.viewmodels.SearchRoleVacancy


@Composable
fun RolesScreen(
    homeScreenViewModel: HomeScreenViewModel,
    searchRoleVacancy: SearchRoleVacancy,
    saveRole: (RoleDetails) -> Unit
) {

    val textColor = White
    val bgColor = BackGroundColor

    val searchText by searchRoleVacancy.searchRoleText.collectAsState()
    val idOfSavedRoles by homeScreenViewModel.listOfSavedRoles.collectAsState()

    // if user is searching than observing search roles else observing all roles

    val roles by if (searchText.isNotEmpty()) {
        searchRoleVacancy.searchedRoles.collectAsState()
    } else {
        homeScreenViewModel.rolesStateFlow.collectAsState()
    }


    val placeholders = listOf("Search by Role", "Search by Name","Search by College Name")
    var currentPlaceholderIndex by remember { mutableIntStateOf(0) }


    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(2000)
            currentPlaceholderIndex = (currentPlaceholderIndex + 1) % placeholders.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (searchBar, filterIcon, divider, rolesList) = createRefs()

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchQuery ->
                    searchRoleVacancy.onSearchRoleTextChange(searchQuery)
                },
                colors = TextFieldStyle.myTextFieldColor(),
                shape = TextFieldStyle.defaultShape,
                maxLines = 1,
                modifier = Modifier
                    .constrainAs(searchBar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth(0.8f),
                placeholder = {

                    Box(
                        modifier = Modifier.animateContentSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = placeholders[currentPlaceholderIndex],
                            style = MaterialTheme.typography.titleMedium,
                            color = textColor
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = White
                    )
                }
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = BorderColor,
                modifier = Modifier.constrainAs(divider) {
                    start.linkTo(parent.start)
                    top.linkTo(searchBar.bottom, margin = 16.dp)
                    end.linkTo(parent.end)
                }
            )

            // Showing list of roles
            ShowListOfRoles(
                modifier = Modifier.constrainAs(rolesList) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(divider.bottom, margin = 10.dp)
                },
                roles,
                homeScreenViewModel, saveRole = {
                    saveRole(it)
                },
                idOfSavedRoles
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowListOfRoles(
    modifier: Modifier,
    roles: List<RoleDetails>,
    homeScreenViewModel: HomeScreenViewModel,
    saveRole: (RoleDetails) -> Unit,
    idOfSavedRoles: List<String>,
) {

    val isLoading by homeScreenViewModel.isRoleLoading.collectAsState()

    LaunchedEffect(Unit) {
        homeScreenViewModel.observeRolesInRealTime()
    }



    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(roles) { role ->
            ShimmerEffect(modifier, isLoading, contentAfterLoading = {
                if (!idOfSavedRoles.contains(role.roleId)) {
                    Log.d("FetchedRole", "Showing unsaved role")
                    SingleRole(role, onSaveRoleClicked = {
                        saveRole(it)
                    }, false)
                }

            })
        }


        item {

            if(roles.isEmpty() || (roles.size-idOfSavedRoles.size)==0) {
                Box( contentAlignment = Alignment.Center) {
                    LoadAnimation(
                        modifier = Modifier.size(200.dp),
                        animation = R.raw.noresult,
                        playAnimation = true
                    )
                }
            }
        }
            item {
                    OutlinedButton(onClick = {
                        homeScreenViewModel.observeRolesInRealTime()
                    }) {
                        Text(text = "Refresh", color = White)
                    }
                Spacer(modifier = Modifier.height(20.dp))
            }
    }

}


