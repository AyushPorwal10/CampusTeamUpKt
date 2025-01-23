package com.example.campus_teamup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.Dimensions
import com.example.campus_teamup.myThemes.PrimaryBlack
import com.example.campus_teamup.myThemes.PrimaryWhiteGradient
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White



@Composable
fun SingleVacancy(modifier: Modifier = Modifier) {

    val textColor = White

    Box(modifier = Modifier.border(0.5.dp , BorderColor , shape = RoundedCornerShape(Dimensions.largeRoundedShape)).
            fillMaxWidth(0.9f)
        , contentAlignment = Alignment.Center) {

        ConstraintLayout(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp).fillMaxWidth() ) {
            val (teamLogo, teamName, roleLookingFor, knowMoreBtn, downIcon) = createRefs()




            Image(painter = painterResource(id = R.drawable.profile),
                contentDescription = null,

                modifier = Modifier
                    .clip(RoundedCornerShape(40.dp))
                    .constrainAs(teamLogo) {
                        top.linkTo(parent.top)

                    })

            Text(text = "Team Name",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.constrainAs(teamName)
                {
                    top.linkTo(teamLogo.top)
                    bottom.linkTo(teamLogo.bottom)
                    start.linkTo(teamLogo.end, margin = 4.dp)
                })

            Text(
                text = "Looking For : Android App Developer",
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                style = MaterialTheme.typography.titleMedium,
                color = LightTextColor,
                modifier = Modifier.constrainAs(roleLookingFor) {
                    top.linkTo(teamLogo.bottom, margin = 12.dp)


                })

            Text(text = "Know more",
                color = textColor,
                fontWeight = FontWeight.Light,
                modifier = Modifier.constrainAs(knowMoreBtn) {
                    top.linkTo(roleLookingFor.bottom, margin = 20.dp)
                    end.linkTo(parent.end)
                })


            Icon(painter = painterResource(id = R.drawable.knowmore),
                contentDescription = null,
                tint = White,
                modifier = Modifier
                    .size(18.dp)
                    .constrainAs(downIcon) {
                        top.linkTo(roleLookingFor.bottom, margin = 20.dp)
                        end.linkTo(parent.end)
                        start.linkTo(knowMoreBtn.end )
                    })
            createHorizontalChain(knowMoreBtn, downIcon, chainStyle = ChainStyle.Packed)
        }
    }





}