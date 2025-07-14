package com.example.new_campus_teamup.roleprofile.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.ShowRequestDialog
import com.example.new_campus_teamup.screens.profilescreens.DetailsCardHeading
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.IconColor
import com.example.new_campus_teamup.viewmodels.ViewProfileViewModel
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.NotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewEducationDetails(
    modifier: Modifier,
    viewProfileViewModel: ViewProfileViewModel,

) {
    var pressed by remember { mutableStateOf(false) }


    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f
    )

    val collegeDetails = viewProfileViewModel.educationDetails.collectAsState()


    Card(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false

                    }
                )
            }
            .fillMaxWidth(0.9f)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFCCC0FE)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {





            // When user click on edit icon show user a dialog to edit education details
            DetailsCardHeading(
                false, onEditButtonClick = {
                },
                "Education Details"
            )

            EducationDetailsField(collegeDetails.value?.collegeName ?: "No details")
            EducationDetailsField(collegeDetails.value?.course ?: "No details")
            EducationDetailsField(collegeDetails.value?.branch ?: "No details")
            EducationDetailsField(collegeDetails.value?.year ?: "No details")




        }
    }
}


@Composable
fun EducationDetailsField(value: String?) {


    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.college),
            tint = IconColor,
            contentDescription = null,
            modifier = Modifier.padding(6.dp)
        )

        Text(value + "", fontWeight = FontWeight.Medium)
    }
}
