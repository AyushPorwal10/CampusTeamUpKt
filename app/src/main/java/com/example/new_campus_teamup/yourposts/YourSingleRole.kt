package com.example.new_campus_teamup.yourposts

import android.content.Intent
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.Dimensions
import com.example.new_campus_teamup.helper.NameFirstAndLastCharacter
import com.example.new_campus_teamup.helper.TimeAndDate
import com.example.new_campus_teamup.myactivities.ViewUserProfile
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.roleprofile.screens.DetailItem
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.RoleCardGradient
import com.example.new_campus_teamup.ui.theme.RoleCardTextColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.ui.theme.WhiteGradient


@Composable
fun YourSingleRole(
    roleDetails: RoleDetails,
    onRoleDelete : (String) -> Unit ,
) {


    val context = LocalContext.current

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
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                if(roleDetails.userImageUrl.isEmpty()) RoleCardGradient else WhiteGradient,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(3.dp)
                        ,
                        contentAlignment = Alignment.Center
                    ) {

                        if(roleDetails.userImageUrl.isEmpty()){
                            Text(NameFirstAndLastCharacter.firstAndLastCharacter(roleDetails.role) , color = White, fontWeight = FontWeight.Bold)
                        }
                        else {
                            AsyncImage(model = roleDetails.userImageUrl.ifBlank { R.drawable.vacancies },
                                contentDescription = null ,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .border(0.5.dp , Color.LightGray , CircleShape)
                            )
                        }
                    }

                    Text(
                        text = roleDetails.userName,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoleCardTextColor
                        )
                    )
                }
                IconButton(
                    onClick = { onRoleDelete(roleDetails.roleId) }
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
                    icon = R.drawable.roles,
                    text = roleDetails.role,

                    )

                DetailItem(
                    icon = R.drawable.college,
                    text = roleDetails.collegeName,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Posted ${TimeAndDate.getTimeAgoFromDate(roleDetails.postedOn)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = RoleCardTextColor
                )
            }
        }
    }
}