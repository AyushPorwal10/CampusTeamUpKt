package com.example.campus_teamup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

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
import com.example.campus_teamup.ui.theme.BackGroundColor

import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White


@Composable
fun SingleRole() {
    val textColor = White
    val bgColor = BackGroundColor

        Box(modifier = Modifier.border(0.5.dp , BorderColor , shape = RoundedCornerShape(22.dp))
            .background(BackGroundColor).fillMaxWidth(0.9f)
            , contentAlignment = Alignment.Center) {

            ConstraintLayout(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp).fillMaxWidth()  ) {
                val (userImage, userName, roleLookingFor , viewProfile , downIcon , knowMoreBtn) = createRefs()



                // Photo of user who posted role
                Image(painter = painterResource(id = R.drawable.profile),
                    contentDescription = null,

                    modifier = Modifier
                        .clip(RoundedCornerShape(40.dp))
                        .constrainAs(userImage) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        })



                // Name of user who posted role
                Text(text = "Ayush Porwal",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor,
                    modifier = Modifier.constrainAs(userName)
                    {
                        start.linkTo(userImage.end, margin = 4.dp)
                        top.linkTo(userImage.top)
                        bottom.linkTo(userImage.bottom)
                    })


                // Role that user posted
                Text(
                    text = "Role : Android App Developer",
                    maxLines = 1,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    softWrap = false,
                    color = LightTextColor,
                    modifier = Modifier.constrainAs(roleLookingFor) {
                        top.linkTo(userImage.bottom, margin = 12.dp)
                        start.linkTo(parent.start)

                    })


                // view Profile btn
                ViewProfileBtn(Modifier.constrainAs(viewProfile){
                    top.linkTo(roleLookingFor.bottom , margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            }
        }




}

@Composable
fun ViewProfileBtn(modifier: Modifier) {
    OutlinedButton(onClick = { /*TODO*/ },
        modifier = modifier,

        colors = ButtonDefaults.buttonColors(
            containerColor = BackGroundColor,
            contentColor = White

        )) {
        Text(text = "View Profile" ,
            fontSize = 12.sp,
            color = White)
    }
}
