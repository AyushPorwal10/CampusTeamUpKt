package com.example.new_campus_teamup.vacancy.screens

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
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.Dimensions
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.ui.theme.BluePrimary

import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.myactivities.ViewVacancy


@Composable
fun SingleVacancy(modifier: Modifier = Modifier, vacancy: VacancyDetails , onSaveVacancy : (VacancyDetails) -> Unit , isSaved : Boolean) {

    val context = LocalContext.current
    val textColor = White
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .border(
                0.5.dp, BorderColor,
                shape = RoundedCornerShape(Dimensions.largeRoundedShape)
            )
            .fillMaxWidth(0.9f)

            .animateContentSize(), contentAlignment = Alignment.Center
    ) {

        ConstraintLayout(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            val (teamLogo, teamName, roleLookingFor, hackathonName, knowMoreBtn, downIcon, applyBtn, skillRequired, saveVacancyBtn) = createRefs()



            AsyncImage(
                model = vacancy.teamLogo ?: R.drawable.profile,
                contentDescription = "User Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .constrainAs(teamLogo) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.dp, White, CircleShape))

            Text(text = vacancy.teamName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.constrainAs(teamName)
                {
                    top.linkTo(teamLogo.top)
                    bottom.linkTo(teamLogo.bottom)
                    start.linkTo(teamLogo.end, margin = 8.dp)
                })

            IconButton(onClick = {
                                 onSaveVacancy(vacancy)
            },
                modifier = Modifier.constrainAs(saveVacancyBtn) {
                    top.linkTo(teamName.top)
                    bottom.linkTo(teamName.bottom)
                    end.linkTo(parent.end)
                }.size(26.dp)) {
                Icon(
                    painter = painterResource(id =  if(isSaved) R.drawable.saved_item else R.drawable.saveproject),
                    contentDescription = null,
                    tint = White
                )
            }

            Text(
                text = "Looking For : ${vacancy.roleLookingFor}",
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                style = MaterialTheme.typography.titleMedium,
                color = LightTextColor,
                modifier = Modifier.constrainAs(roleLookingFor) {
                    top.linkTo(teamLogo.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                })

            if (isExpanded) {
                Text(text = "Hackathon : ${vacancy.hackathonName}", color = LightTextColor,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(hackathonName) {
                            top.linkTo(roleLookingFor.bottom, margin = 4.dp)
                            start.linkTo(parent.start)
                        })

                Text(text = "Skills Required : ${vacancy.skills}", color = BluePrimary,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(skillRequired) {
                            top.linkTo(hackathonName.bottom, margin = 4.dp)
                            start.linkTo(parent.start)
                        })


                TextButton(
                    onClick = {
                        val intent = Intent(context, ViewVacancy::class.java)
                        intent.putExtra("vacancy_details", vacancy)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.constrainAs(applyBtn) {
                        top.linkTo(skillRequired.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                    }, contentPadding = PaddingValues(vertical = 2.dp, horizontal = 10.dp)
                ) {
                    Text(
                        text = "View Details",
                        color = White,
                        fontSize = 12.sp
                    )
                }
            }

            Text(text = if (isExpanded) stringResource(R.string.view_less) else stringResource(R.string.view_more),
                color = textColor,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .constrainAs(knowMoreBtn) {
                        top.linkTo(
                            if (isExpanded) skillRequired.bottom else roleLookingFor.bottom,
                            margin = 20.dp
                        )
                    }
                    .clickable {
                        isExpanded = !isExpanded
                    })


            Icon(painter = painterResource(id = R.drawable.knowmore),
                contentDescription = null,
                tint = White,
                modifier = Modifier
                    .size(18.dp)
                    .constrainAs(downIcon) {
                        top.linkTo(
                            if (isExpanded) skillRequired.bottom else roleLookingFor.bottom,
                            margin = 20.dp
                        )
                        end.linkTo(knowMoreBtn.end)
                        start.linkTo(knowMoreBtn.end)
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isExpanded = !isExpanded
                    })
            createHorizontalChain(knowMoreBtn, downIcon, chainStyle = ChainStyle.Packed)

            if (isExpanded) {
                createHorizontalChain(applyBtn, knowMoreBtn, chainStyle = ChainStyle.Spread)
            }
        }
    }


}