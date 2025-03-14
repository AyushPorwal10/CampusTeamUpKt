package com.example.campus_teamup.vacancy.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.NotificationViewModel
import com.example.campus_teamup.viewmodels.ViewVacancyViewModel

@Composable
fun ViewTeamDetails(
    modifier: Modifier = Modifier,
    viewVacancyViewModel: ViewVacancyViewModel,
) {


    val userIdWithMap = viewVacancyViewModel.userIdWithMap.collectAsState()

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp) , horizontalAlignment = Alignment.CenterHorizontally) {
        userIdWithMap.value.forEach { (userId, imageUrl) ->
            TeamMember(memberId = userId, imageUrl = imageUrl)
        }
    }
}


@Composable
fun TeamMember(memberId: String, imageUrl: String) {


    ConstraintLayout(modifier = Modifier
        .fillMaxWidth(0.9f)
        .clip(TextFieldStyle.defaultShape)
        .background(BorderColor)) {
        val (userImage, userName, viewProfileBtn, sendMessageBtn) = createRefs()


        AsyncImage(
            model = imageUrl.ifEmpty { R.drawable.profile },
            contentDescription = "User Profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(1.dp, White, CircleShape)
                .padding(4.dp)
                .constrainAs(userImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        Text(
            text = memberId, style = MaterialTheme.typography.titleMedium, color = White,
            modifier = Modifier
                .padding(4.dp)
                .constrainAs(userName) {
                    start.linkTo(userImage.end, margin = 6.dp)
                    top.linkTo(userImage.top)
                    bottom.linkTo(userImage.bottom)
                }
        )
    }


}