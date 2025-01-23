package com.example.campus_teamup.screens

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.Dimensions
import com.example.campus_teamup.ui.theme.Black




import com.example.campus_teamup.ui.theme.White

@Preview
@Composable
fun SingleNotification() {


    val textColor = if (isSystemInDarkTheme()) White else Black
    val bgColor = if(isSystemInDarkTheme()) Black else White
    Box(modifier = Modifier.fillMaxWidth(0.9f)
        .background(bgColor)
        .border(0.5.dp, textColor, shape = RoundedCornerShape(Dimensions.smallRoundedShape))
        , contentAlignment = Alignment.Center
    ){

        ConstraintLayout(modifier = Modifier.padding(15.dp).fillMaxWidth(1f)
        ) {
            val (userImage, userName, acceptBtn , rejectBtn) = createRefs()
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
                text = "Ayush Porwal Request to Join" ,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                color = textColor,
                overflow = TextOverflow.Ellipsis,

                fontSize = 18.sp,
                modifier = Modifier.constrainAs(userName){
                    top.linkTo(userImage.top)
                    start.linkTo(userImage.end , margin = 4.dp)
                    bottom.linkTo(userImage.bottom)
                    end.linkTo(parent.end)
                }.fillMaxWidth(0.75f) )


            Icon(
                painter = painterResource(id = R.drawable.acceptbtn),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .size(24.dp)
                    .constrainAs(acceptBtn) {
                        top.linkTo(userImage.bottom, margin = 4.dp)
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
                        top.linkTo(userImage.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 4.dp)
                        bottom.linkTo(parent.bottom)


                    } , tint = textColor)
            createHorizontalChain(acceptBtn , rejectBtn , chainStyle = ChainStyle.Spread )


        }
    }
}