package com.example.campus_teamup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.ui.theme.BackGroundColor

import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White


@Composable
fun SingleRole(roleDetails: RoleDetails) {
    val textColor = White
    val bgColor = BackGroundColor

    Box(
        modifier = Modifier
            .border(0.5.dp, BorderColor, shape = RoundedCornerShape(22.dp))
            .fillMaxWidth(0.9f), contentAlignment = Alignment.Center
    ) {

        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth()
        ) {
            val (userImage, userName, roleLookingFor, viewProfile, downIcon, knowMoreBtn) = createRefs()


            // Photo of user who posted role

            AsyncImage(
                model = roleDetails.userImageUrl,
                contentDescription = "User Profile",
                contentScale = ContentScale.Crop,
                //placeholder = painterResource(R.drawable.profile),
                error = painterResource(R.drawable.profile),
                modifier = Modifier
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(40.dp)
                    .clip(CircleShape)
            )



            // Name of user who posted role
            Text(text = roleDetails.userName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                modifier = Modifier.constrainAs(userName)
                {
                    start.linkTo(userImage.end, margin = 8.dp)
                    top.linkTo(userImage.top)
                    bottom.linkTo(userImage.bottom)
                })


            // Role that user posted
            Text(
                text = "Role : ${roleDetails.role}",
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                softWrap = false,
                color = LightTextColor,
                modifier = Modifier.constrainAs(roleLookingFor) {
                    top.linkTo(userImage.bottom, margin = 12.dp)
                    start.linkTo(parent.start)

                })


            // view Profile btn
            ViewProfileBtn(Modifier.constrainAs(viewProfile) {
                top.linkTo(roleLookingFor.bottom, margin = 10.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        }
    }


}

@Composable
fun ViewProfileBtn(modifier: Modifier) {
    TextButton(
        onClick = { /*TODO*/ },
        modifier = modifier,

        colors = ButtonDefaults.buttonColors(
            containerColor = BackGroundColor,
            contentColor = White

        )
    ) {
        Text(
            text = "View Profile",
            fontSize = 12.sp,
            color = White
        )
    }
}
