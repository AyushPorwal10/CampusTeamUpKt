package com.example.campus_teamup.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.Dimensions
import com.example.campus_teamup.ui.theme.BluePrimary

import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White



@Composable
fun SingleVacancy(modifier: Modifier = Modifier) {

    val textColor = White
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .border(
            0.5.dp, BorderColor,
            shape = RoundedCornerShape(Dimensions.largeRoundedShape)
        )
        .fillMaxWidth(0.9f)

        .animateContentSize()
        , contentAlignment = Alignment.Center) {

        ConstraintLayout(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth() ) {
            val (teamLogo, teamName, roleLookingFor, knowMoreBtn, downIcon, applyBtn , skillRequired) = createRefs()




            Image(painter = painterResource(id = R.drawable.profile),
                contentDescription = null,

                modifier = Modifier
                    .clip(RoundedCornerShape(40.dp))
                    .constrainAs(teamLogo) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
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
                    start.linkTo(parent.start)
                })

            if(isExpanded){
                Text(text = "Skill Required : Kotlin , Jetpack Compose", color = BluePrimary ,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(skillRequired) {
                            top.linkTo(roleLookingFor.bottom, margin = 4.dp)
                            start.linkTo(parent.start)
                        })
                OutlinedButton(onClick = { /*TODO*/ } ,
                    modifier = Modifier.constrainAs(applyBtn){
                    top.linkTo(skillRequired.bottom , margin = 4.dp)
                    start.linkTo(parent.start)
                } , contentPadding = PaddingValues(vertical = 2.dp , horizontal = 10.dp)) {
                    Text(text = "View and Apply" ,
                        color = White ,
                        fontSize = 12.sp)
                }
            }



            Text(text = "Know more",
                color = textColor,
                fontWeight = FontWeight.Light,
                modifier = Modifier.constrainAs(knowMoreBtn) {
                    top.linkTo(if (isExpanded) skillRequired.bottom else roleLookingFor.bottom, margin = 20.dp)

                })


            Icon(painter = painterResource(id = R.drawable.knowmore),
                contentDescription = null,
                tint = White,
                modifier = Modifier
                    .size(18.dp)
                    .constrainAs(downIcon) {
                        top.linkTo(if (isExpanded) skillRequired.bottom else roleLookingFor.bottom, margin = 20.dp)
                        end.linkTo(knowMoreBtn.end)
                        start.linkTo(knowMoreBtn.end)
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isExpanded = !isExpanded
                    })
            createHorizontalChain(knowMoreBtn, downIcon, chainStyle = ChainStyle.Packed)

            if(isExpanded){
                createHorizontalChain(applyBtn , knowMoreBtn , chainStyle = ChainStyle.Spread)
            }
        }
    }





}