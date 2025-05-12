package com.example.new_campus_teamup.yourposts

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.CheckEmptyFields
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.White


@Composable
fun YourSingleProject(projectDetails: ProjectDetails, onProjectDelete: (String) -> Unit) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

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
            val (teamName, saveProject, hackathonName, problemDescription, projectUrl) = createRefs()

            Text(text = "Team : ${projectDetails.teamName}",
                color = White,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .constrainAs(teamName) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(parent.start)
                    }
                   )


            IconButton(onClick = {
                onProjectDelete(projectDetails.projectId)
            },
                modifier = Modifier
                    .constrainAs(saveProject) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .size(26.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.delete),
                    contentDescription = null,
                    tint = White
                )
            }
            //createHorizontalChain(teamName, saveProject, chainStyle = ChainStyle.Packed)


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

            Text(
                text = "GitHub URL : ${projectDetails.githubUrl}",
                color = White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .constrainAs(projectUrl) {
                        top.linkTo(problemDescription.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                    }
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
    }
}
