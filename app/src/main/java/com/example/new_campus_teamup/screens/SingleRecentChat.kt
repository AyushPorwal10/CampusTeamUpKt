package com.example.new_campus_teamup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.new_campus_teamup.helper.Dimensions
import com.example.new_campus_teamup.mydataclass.RecentChats
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.LightWhite
import com.example.new_campus_teamup.ui.theme.White


@Composable
fun SingleRecentChat(chatData : RecentChats , onClick : () -> Unit){
    val textColor = LightTextColor
    val bgColor = BackGroundColor

    Box(modifier = Modifier
        .clickable {
         onClick()
        }
        .background(bgColor)
        .padding(8.dp)
        .border(0.5.dp, BorderColor, shape = RoundedCornerShape(Dimensions.smallRoundedShape))
        , contentAlignment = Alignment.Center
    ){

        ConstraintLayout(modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()) {
            val ( userName , notificationCount , lastMessageDate) = createRefs()

            Text(
                text = chatData.senderName,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                color = White,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .constrainAs(userName) {
                        start.linkTo(parent.start, margin = 2.dp)
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth(0.75f) )


            Text(text = "Created on ${chatData.createdOn}" ,
                style = MaterialTheme.typography.bodySmall ,
                color = LightWhite,
                modifier = Modifier.constrainAs(lastMessageDate){
                    top.linkTo(userName.bottom , margin = 4.dp)
                    start.linkTo(parent.start)
                })

        }
    }
}