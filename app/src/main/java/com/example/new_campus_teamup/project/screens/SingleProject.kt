package com.example.new_campus_teamup.project.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.CheckEmptyFields
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.LightWhite
import com.example.new_campus_teamup.ui.theme.White

@Composable
fun SingleProject(projectDetails: ProjectDetails, onSaveProjectClicked : (String)  -> Unit , isSaved : Boolean) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    Log.d("FetchingProjects","Single project showing ${projectDetails.projectId}")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(1.dp, BorderColor, RoundedCornerShape(22.dp)),
        contentAlignment = Alignment.Center,
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            val (teamName, saveProject, hackathonName, problemDescription, viewMoreOrLess, viewTeamDetails, projectUrl, divider) = createRefs()

            Text(text = "Team : ${projectDetails.teamName}",
                color = White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .constrainAs(teamName) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(0.8f))


            IconButton(onClick = {
                Log.d("Saving","Going to save Project id ${projectDetails.projectId}")

                onSaveProjectClicked(projectDetails.projectId)
            },
                modifier = Modifier
                    .constrainAs(saveProject) {
                        top.linkTo(parent.top)
                        start.linkTo(teamName.end)
                        end.linkTo(parent.end)
                    }.size(26.dp)) {
                Icon(
                    painterResource(id =  if(isSaved) R.drawable.saved_item else R.drawable.saveproject),
                    contentDescription = stringResource(
                        id = R.string.save_project
                    ),
                    tint = White
                )
            }
            createHorizontalChain(teamName, saveProject, chainStyle = ChainStyle.Packed)


            Text(
                text = "${projectDetails.hackathonOrPersonal} Project",
                color = White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(hackathonName) {
                    top.linkTo(teamName.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                }
            )

            Text(
                text = "Problem Statement : ${projectDetails.problemStatement}",
                color = LightTextColor,

                maxLines = if (isExpanded) 50 else 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(problemDescription) {
                    top.linkTo(hackathonName.bottom, margin = 6.dp)
                    start.linkTo(parent.start)
                }
            )


            if (isExpanded) {
                Text(
                    text = "GitHub URL : ${projectDetails.githubUrl}",
                    color = Color.Blue,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .constrainAs(projectUrl) {
                            top.linkTo(problemDescription.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                        }
                        .animateContentSize()
                        .clickable {
                            if(CheckEmptyFields.isValidHttpsUrl(projectDetails.githubUrl)){
                                val browseLink = Intent(Intent.ACTION_VIEW , Uri.parse(projectDetails.githubUrl))
                                context.startActivity(browseLink)
                            }
                            else {
                                ToastHelper.showToast(context , "Invalid url.")
                            }
                        }
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightTextColor)
                    .constrainAs(divider) {
                        top.linkTo(
                            if (isExpanded) projectUrl.bottom else problemDescription.bottom,
                            margin = 6.dp
                        )
                    })

            TextButton(onClick = {
                isExpanded = !isExpanded
            },
                modifier = Modifier.constrainAs(viewMoreOrLess) {

                    top.linkTo(
                        if (isExpanded) projectUrl.bottom else divider.bottom,
                        margin = 6.dp
                    )
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)


                }) {

                Text(text = if (isExpanded) "View Less" else "View More", color = LightWhite)
            }

        }
    }
}
