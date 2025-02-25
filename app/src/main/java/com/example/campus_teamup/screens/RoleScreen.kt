package com.example.campus_teamup.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.ShimmerEffect
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
    searchRoleVacancy: SearchRoleVacancy
) {
    val textColor = White
    val bgColor = BackGroundColor

    val searchText by searchRoleVacancy.searchRoleText.collectAsState()

    val isSearching by searchRoleVacancy.isRoleSearching.collectAsState()

    // if user is searching than observing search roles else observing all roles

    val roles by if (searchText.isNotEmpty()) {
        searchRoleVacancy.searchedRoles.collectAsState()
    } else {
        homeScreenViewModel.rolesStateFlow.collectAsState()
    }



    val placeholders = listOf("Search by Role", "Search by Name")
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
            .background(bgColor)
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

            Icon(
                painter = painterResource(id = R.drawable.filter),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
                    .constrainAs(filterIcon) {
                        start.linkTo(searchBar.end, margin = 8.dp)
                        end.linkTo(parent.end)
                        top.linkTo(searchBar.top)
                        bottom.linkTo(searchBar.bottom)
                    }
                    .fillMaxWidth(0.1f),
                tint = White
            )

            // Horizontal arrangement of search items
            createHorizontalChain(searchBar, filterIcon, chainStyle = ChainStyle.Spread)

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
                homeScreenViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowListOfRoles(
    modifier: Modifier,
    roles: List<RoleDetails>,
    homeScreenViewModel: HomeScreenViewModel
) {
    var isRefreshing = homeScreenViewModel.isRoleRefreshing.collectAsState()

    val isLoading by homeScreenViewModel.isRoleLoading.collectAsState()

    LaunchedEffect(Unit) {
        homeScreenViewModel.observeRolesInRealTime()
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing.value,
        onRefresh = {
            homeScreenViewModel.observeRolesInRealTime()
        }, modifier = modifier, contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(roles) { role ->
                ShimmerEffect(modifier, isLoading, contentAfterLoading = {
                    SingleRole(role)
                })
            }

        }


    }
}


