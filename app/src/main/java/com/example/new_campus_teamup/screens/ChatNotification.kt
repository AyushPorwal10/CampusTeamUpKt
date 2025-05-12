package com.example.new_campus_teamup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.Dimensions
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightTextColor


@Composable
fun ChatNotification(){

    val textColor = LightTextColor
    val bgColor = BackGroundColor

    Box(modifier = Modifier
        .fillMaxWidth(0.9f)
        .background(bgColor)
        .padding(top = 8.dp)
        .border(0.5.dp, BorderColor, shape = RoundedCornerShape(Dimensions.smallRoundedShape))
        , contentAlignment = Alignment.Center
    ){

        ConstraintLayout(modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
        ) {
            val (userImage, userName) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                colorFilter = ColorFilter.tint(textColor),
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .constrainAs(userImage) {


                    })

            Text(
                text = "New Messages" ,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                color = textColor,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .constrainAs(userName) {
                        start.linkTo(userImage.end , margin = 2.dp)
                        top.linkTo(userImage.top)
                        bottom.linkTo(userImage.bottom)
                    }
                    .fillMaxWidth() )



        }
    }
}