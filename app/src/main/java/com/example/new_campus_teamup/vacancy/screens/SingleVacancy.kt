package com.example.new_campus_teamup.vacancy.screens

import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.new_campus_teamup.helper.NameFirstAndLastCharacter
import com.example.new_campus_teamup.helper.TimeAndDate
import com.example.new_campus_teamup.myactivities.ViewVacancy
import com.example.new_campus_teamup.mydataclass.VacancyDetails
import com.example.new_campus_teamup.roleprofile.screens.DetailItem
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.BluePrimary
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.RoleCardGradient
import com.example.new_campus_teamup.ui.theme.RoleCardTextColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.ui.theme.WhiteGradient


@Composable
fun SingleVacancyCard(
    modifier: Modifier = Modifier,
    vacancy: VacancyDetails,
    onSaveVacancy: (VacancyDetails) -> Unit,
    isSaved: Boolean
) {

    val isExpanded = remember { mutableStateOf(false) }

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
                                if(vacancy.teamLogo.isEmpty()) RoleCardGradient else WhiteGradient,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(3.dp)
                           ,
                        contentAlignment = Alignment.Center
                    ) {

                        if(vacancy.teamLogo.isEmpty()){
                            Text(NameFirstAndLastCharacter.firstAndLastCharacter(vacancy.teamName) , color = White, fontWeight = FontWeight.Bold)
                        }
                        else {
                            AsyncImage(model = vacancy.teamLogo.ifBlank { R.drawable.vacancies },
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
                        text = vacancy.teamName,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Black
                        )
                    )
                }

                IconButton(
                    onClick = { onSaveVacancy(vacancy) }
                ) {
                    Icon(
                        painter = painterResource(if (isSaved) R.drawable.saved_item else R.drawable.saveproject),
                        contentDescription = "Save",
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DetailItem(
                    icon = R.drawable.roles,
                    text = "Looking for "+ vacancy.roleLookingFor,

                    )

                DetailItem(
                    icon = R.drawable.hackathon,
                    text = vacancy.hackathonName,
                )
                DetailItem(
                    icon = R.drawable.skills,
                    text = vacancy.skills,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, ViewVacancy::class.java)
                        intent.putExtra("vacancy_details", vacancy)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .background(
                            RoleCardGradient,
                            RoundedCornerShape(12.dp)
                        ),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        "View More",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = "Posted ${TimeAndDate.getTimeAgoFromDate(vacancy.postedOn)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = RoleCardTextColor
                )
            }
        }
    }
}