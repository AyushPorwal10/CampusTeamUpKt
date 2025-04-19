package com.example.campus_teamup.roleprofile.screens

import android.content.Intent
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.campus_teamup.helper.TimeAndDate
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.myactivities.ViewUserProfile
import com.example.campus_teamup.ui.theme.BackGroundColor

import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White


@Composable
fun SingleRole(
    roleDetails: RoleDetails,
    onSaveRoleClicked: (RoleDetails) -> Unit,
    isSaved: Boolean
) {

    val context = LocalContext.current
    val textColor = White

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
            val (userImage, userName, roleLookingFor, viewProfileBtn, collegeName  , postedOn, saveProjectBtn) = createRefs()



            AsyncImage(
                model = roleDetails.userImageUrl.ifEmpty { R.drawable.profile },
                contentDescription = "User Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.dp, White, CircleShape))

            Text(text = roleDetails.userName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.constrainAs(userName)
                {
                    top.linkTo(userImage.top)
                    bottom.linkTo(userImage.bottom)
                    start.linkTo(userImage.end, margin = 8.dp)
                })

            IconButton(onClick = {
                onSaveRoleClicked(roleDetails)
            }, modifier = Modifier
                .constrainAs(saveProjectBtn) {
                    top.linkTo(parent.top)
                    bottom.linkTo(userName.bottom)
                    end.linkTo(parent.end)
                }
                .size(26.dp)) {
                Icon(
                    painter = painterResource(id = if (isSaved) R.drawable.saved_item else R.drawable.saveproject),
                    contentDescription = null,
                    tint = White
                )

            }


            Text(
                text = "Role : ${roleDetails.role}",
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                style = MaterialTheme.typography.titleMedium,
                color = LightTextColor,
                modifier = Modifier.constrainAs(roleLookingFor) {
                    top.linkTo(userImage.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                })


            Text(
                text = "College : ${roleDetails.collegeName}",
                maxLines = 2,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                style = MaterialTheme.typography.titleMedium,
                color = LightTextColor,
                modifier = Modifier.constrainAs(collegeName) {
                    top.linkTo(roleLookingFor.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                })




            TextButton(
                onClick = {
                    val intent = Intent(context, ViewUserProfile::class.java)
                    Log.d(
                        "FCM",
                        "User id taken from vacancy activity ${roleDetails.postedBy} <- here it is "
                    )
                    intent.putExtra("userId", roleDetails.postedBy)
                    intent.putExtra("phone_number",roleDetails.phoneNumber)
                    context.startActivity(intent)
                },
                modifier = Modifier.constrainAs(viewProfileBtn) {
                    top.linkTo(collegeName.bottom, margin = 4.dp)
                }, contentPadding = PaddingValues(vertical = 2.dp, horizontal = 10.dp)
            ) {
                Text(
                    text = "View Profile",
                    color = White,
                    fontSize = 12.sp
                )
            }

            TextButton(onClick = {  } , enabled = false, modifier = Modifier.constrainAs(postedOn) {
                top.linkTo(collegeName.bottom, margin = 4.dp)
            }) {

                Text(
                    text = "Posted ${TimeAndDate.getTimeAgoFromDate(roleDetails.postedOn)}",
                    color = LightTextColor,
                    fontSize = 12.sp
                )

            }
            createHorizontalChain(viewProfileBtn, postedOn, chainStyle = ChainStyle.Spread)

        }
    }


}

