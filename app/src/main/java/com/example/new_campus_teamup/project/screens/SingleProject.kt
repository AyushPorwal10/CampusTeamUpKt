package com.example.new_campus_teamup.project.screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.roleprofile.screens.DetailItem
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.LightBlueColor

@Composable
fun SingleProject(
    projectDetails: ProjectDetails,
    onSaveProjectClicked: (String) -> Unit,
    onReportProjectBtnClick: () -> Unit = {},
    isSaved: Boolean
) {
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

                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        onReportProjectBtnClick()
                    }, modifier = Modifier.size(28.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.report_post),
                            contentDescription = null,
                            tint = Color.Red,
                        )
                    }

                    IconButton(
                        onClick = { onSaveProjectClicked(projectDetails.projectId) }
                    ) {
                        Icon(
                            painter = painterResource(if (isSaved) R.drawable.saved_item else R.drawable.saveproject),
                            contentDescription = "Save",
                        )
                    }
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
