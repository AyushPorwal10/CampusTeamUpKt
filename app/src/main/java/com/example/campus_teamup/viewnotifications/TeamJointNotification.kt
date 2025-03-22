package com.example.campus_teamup.viewnotifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.Dimensions
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White

@Composable
fun TeamJointNotification(memberInviteNotification: NotificationItems.MemberInviteNotification) {


    val textColor = LightTextColor
    val bgColor = BackGroundColor

    Box(modifier = Modifier
        .fillMaxWidth(0.9f)
        .background(BorderColor)
        .border(0.5.dp, BorderColor, shape = RoundedCornerShape(Dimensions.smallRoundedShape))
        , contentAlignment = Alignment.Center
    ){

        ConstraintLayout(modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(1f)
        ) {
            val (userImage, userName, acceptBtn , rejectBtn , viewProfileBtn) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                colorFilter = ColorFilter.tint(textColor),
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)

                    })

            Text(
                text = "${memberInviteNotification.senderName} wants to join your Team." ,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                style = MaterialTheme.typography.titleMedium,
                color = White,
                overflow = TextOverflow.Ellipsis,

                fontSize = 18.sp,
                modifier = Modifier
                    .constrainAs(userName) {
                        top.linkTo(userImage.top)
                        start.linkTo(userImage.end, margin = 2.dp)
                        bottom.linkTo(userImage.bottom)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(0.75f) )

            TextButton(onClick = {

            },modifier = Modifier.constrainAs(viewProfileBtn){
                top.linkTo(userName.bottom , margin = 6.dp)
            }) {
                Text(text = "View Profile" , color = White , style = MaterialTheme.typography.titleSmall)
            }

            Icon(
                painter = painterResource(id = R.drawable.acceptbtn),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .size(24.dp)
                    .constrainAs(acceptBtn) {
                        top.linkTo(viewProfileBtn.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    } , tint = textColor)

            Icon(
                painter = painterResource(id = R.drawable.rejectbtn),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .size(24.dp)
                    .constrainAs(rejectBtn) {
                        top.linkTo(viewProfileBtn.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)


                    } , tint = textColor)
            createHorizontalChain(acceptBtn , rejectBtn , chainStyle = ChainStyle.Spread )
        }
    }
}