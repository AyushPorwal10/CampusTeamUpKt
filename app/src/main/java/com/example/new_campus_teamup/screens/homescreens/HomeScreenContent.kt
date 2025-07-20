package com.example.new_campus_teamup.screens.homescreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myAnimation.FloatingBubbles
import com.example.new_campus_teamup.mysealedClass.BottomNavScreens
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.IconColor


@Composable
fun HomeScreenContent(modifier: Modifier, navController: NavController, userName: String) {

    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = BackgroundGradientColor,
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    ) {
        FloatingBubbles()
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RoundedBottomRectangle(userName)
            ExploreOpportunities()
            RolesCard{
                navController.navigate(BottomNavScreens.Roles.screen)
            }
            VacancyCard{
                navController.navigate(BottomNavScreens.Vacancies.screen)
            }
            FounderHubCard{
                ToastHelper.showToast(context , "Feature coming soon..")
            }
            CPTeamUp{
                ToastHelper.showToast(context , "Feature coming soon..")
            }
            ProjectCard {
                navController.navigate(BottomNavScreens.Projects.screen)
            }
        }

    }


}

@Composable
fun ExploreOpportunities() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), horizontalArrangement = Arrangement.Start
    ) {

        Text(
            stringResource(R.string.explore_opportunities),
            style = MaterialTheme.typography.titleLarge,
            color = IconColor
        )
    }
}

@Composable
fun RoundedBottomRectangle(userName: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFEFEEFF),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 28.dp,
                    bottomEnd = 28.dp
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEFEEFF)
        ),
        elevation = CardDefaults.cardElevation(
            16.dp
        )
    ) {
            Column() {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Hello ",
                        modifier = Modifier.padding(start = 10.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        painter = painterResource(R.drawable.hello),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )

                }
                Text(
                    userName,
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
    }
}
