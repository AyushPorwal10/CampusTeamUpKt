package com.example.new_campus_teamup.roleprofile.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.helper.ShimmerEffect
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.HomeScreenViewModel
import com.example.new_campus_teamup.viewmodels.SearchRoleVacancy

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

    val roles by if (searchText.isNotEmpty()) {
        searchRoleVacancy.searchedRoles.collectAsState()
    } else {
        homeScreenViewModel.rolesStateFlow.collectAsState()
    }

    val placeholders = listOf("Search by Role", "Search by Name", "Search by College Name")
    var currentPlaceholderIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(2000)
            currentPlaceholderIndex = (currentPlaceholderIndex + 1) % placeholders.size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchQuery ->
                searchRoleVacancy.onSearchRoleTextChange(searchQuery)
            },
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(top = 16.dp),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        )

        Box(modifier = Modifier.weight(1f)) {
            ShowListOfRoles(
                modifier = Modifier.fillMaxSize(),
                roles = roles,
                homeScreenViewModel = homeScreenViewModel,
                saveRole = saveRole,
                idOfSavedRoles = idOfSavedRoles
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



    val filteredRoles = roles.filter { !idOfSavedRoles.contains(it.roleId) }

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filteredRoles) { role ->
            ShimmerEffect(modifier, isLoading, contentAfterLoading = {

                    Log.d("FetchedRole", "Showing unsaved role")
                    SingleRole(role, onSaveRoleClicked = {
                        saveRole(it)
                    }, false)


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

    }

}


