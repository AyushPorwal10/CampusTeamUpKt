package com.example.new_campus_teamup.project.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.CheckEmptyFields
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.roleprofile.screens.DetailItem
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightBlueColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.LightWhite
import com.example.new_campus_teamup.ui.theme.White

@Composable
fun SingleProject(projectDetails: ProjectDetails, onSaveProjectClicked : (String)  -> Unit , isSaved : Boolean) {
    var isHovered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.02f else 1f,
        animationSpec = tween(300),
        label = "scale"
    )



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .padding(20.dp)
            .clickable { isHovered = !isHovered },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    Text(
                        text = "Team : " + projectDetails.teamName,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Black
                        )
                    )
                }

                IconButton(onClick = {
                    Log.d("Saving","Going to save Project id ${projectDetails.projectId}")

                    onSaveProjectClicked(projectDetails.projectId)
                },
                    modifier = Modifier
                        .size(26.dp)) {
                    Icon(
                        painterResource(id =  if(isSaved) R.drawable.saved_item else R.drawable.saveproject),
                        contentDescription = stringResource(
                            id = R.string.save_project
                        ),
                        tint = Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DetailItem(
                    icon = R.drawable.hackathon,
                    text = projectDetails.hackathonOrPersonal,
                )

                DetailItem(
                    icon = R.drawable.problem_statement,
                    text = projectDetails.problemStatement,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Github : " + projectDetails.githubUrl,
                    style = MaterialTheme.typography.bodySmall,
                    color = LightBlueColor
                )
            }

        }
    }
}
