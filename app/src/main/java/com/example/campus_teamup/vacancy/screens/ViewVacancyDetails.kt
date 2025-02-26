package com.example.campus_teamup.vacancy.screens

import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.Dimensions
import com.example.campus_teamup.mydataclass.VacancyDetails
import com.example.campus_teamup.ui.theme.BluePrimary
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White

@Composable
fun ViewVacancyDetails(modifier: Modifier = Modifier, vacancy: VacancyDetails) {
    val textColor = White
    Box(
        modifier = modifier
            .border(
                0.5.dp, BorderColor,
                shape = RoundedCornerShape(Dimensions.largeRoundedShape)
            )
            .fillMaxWidth(0.9f), contentAlignment = Alignment.Center
    ) {

        ConstraintLayout(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            val (teamLogo, teamName, roleLookingFor, hackathonName, roleDescription, datePosted, skillRequired) = createRefs()



            AsyncImage(
                model = vacancy.teamLogo,
                contentDescription = "User Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .constrainAs(teamLogo) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, White, CircleShape))

            Text(text = "Team : ${vacancy.teamName}",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.constrainAs(teamName)
                {
                    top.linkTo(teamLogo.bottom , margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Text(
                text = "Looking For : ${vacancy.roleLookingFor}",
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                style = MaterialTheme.typography.titleMedium,
                color = LightTextColor,
                modifier = Modifier.constrainAs(roleLookingFor) {
                    top.linkTo(teamName.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                })

            Text(text = "Hackathon : ${vacancy.hackathonName}", color = LightTextColor,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(hackathonName) {
                        top.linkTo(roleLookingFor.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    })

            Text(text = "Skills Required : ${vacancy.skills}", color = BluePrimary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(skillRequired) {
                        top.linkTo(hackathonName.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    })

            Text(text = "Role Description : " + vacancy.roleDescription.ifEmpty { "No Description" },
            color = LightTextColor,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(roleDescription) {
                    top.linkTo(skillRequired.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                })

            Text(text = "Posted on : ${vacancy.postedOn}", color = White,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(datePosted) {
                        top.linkTo(roleDescription.bottom, margin = 8.dp)
                        start.linkTo(roleDescription.end)
                        end.linkTo(parent.end)
                    })
        }
    }
}


