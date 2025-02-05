package com.example.campus_teamup.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.PrimaryBlack
import com.example.campus_teamup.myThemes.PrimaryWhiteGradient
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightWhite
import com.example.campus_teamup.ui.theme.White



@Composable
fun RolesScreen() {
    val textColor = White
    val bgColor = BackGroundColor

    var queryText by remember { mutableStateOf("") }
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
                value = queryText,
                onValueChange = { searchQuery -> queryText = searchQuery },
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
                }
            )
        }
    }
}

@Composable
fun ShowListOfRoles(modifier: Modifier) {
    LazyColumn(modifier = modifier , verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(10) {
            SingleRole()
        }
    }
}


