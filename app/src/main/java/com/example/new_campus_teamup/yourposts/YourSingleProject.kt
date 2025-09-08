package com.example.new_campus_teamup.yourposts

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.mydataclass.ProjectDetails
import com.example.new_campus_teamup.roleprofile.screens.DetailItem
import com.example.new_campus_teamup.ui.theme.LightBlueColor
import com.example.new_campus_teamup.ui.theme.RoleCardTextColor


@Composable
fun YourSingleProject(projectDetails: ProjectDetails, onProjectDelete: (String) -> Unit) {

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
            .padding(8.dp)
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
                            color = RoleCardTextColor
                        )
                    )
                }

                IconButton(
                    onClick = { onProjectDelete(projectDetails.projectId) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.delete),
                        contentDescription = "Delete",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
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
