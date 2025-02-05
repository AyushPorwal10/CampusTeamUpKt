package com.example.campus_teamup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.ui.theme.BluePrimary
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.LightWhite
import com.example.campus_teamup.ui.theme.White

@Composable
fun SingleProject() {
    val expanded by remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier
        .fillMaxWidth().padding(10.dp)
        .border(1.dp , BorderColor , RoundedCornerShape(22.dp))
        , contentAlignment = Alignment.Center,){

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
            val (teamName , saveProject , hackathonName , problemDescription , likeProject , viewMoreOrLess ,viewTeamDetails ,  projectCategory , divider) = createRefs()

            Text(text = "Team : 4 Errors" ,
                color = White,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .constrainAs(teamName) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(0.8f))


            IconButton(onClick = { },
                modifier = Modifier
                    .constrainAs(saveProject) {
                        top.linkTo(parent.top)
                        start.linkTo(teamName.end)
                        end.linkTo(parent.end)
                    }) {
                Icon(
                    painterResource(id = R.drawable.saveproject) , contentDescription = stringResource(
                    id = R.string.save_project
                ) , tint = White
                )
            }
            createHorizontalChain(teamName , saveProject , chainStyle = ChainStyle.Packed)


            Text(
                text = "Hachathon : Amazon Hackon",
                color = White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(hackathonName){
                    top.linkTo(teamName.bottom , margin = 10.dp)
                    start.linkTo(parent.start)

                }
            )

            Text(
                text = "Problem Statement : To Build a web app that helps business to manage their " +
                        "supply chain and help to manage waste.To Build a web app that helps business" +
                        " to manage their supply chain and help to manage waste",
                color = LightTextColor,

                maxLines = if(expanded) 50 else 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(problemDescription){
                    top.linkTo(hackathonName.bottom , margin = 6.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )


            if(expanded){
                Text(
                    text = "Category : Android App",
                    color = White,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.constrainAs(projectCategory){
                        top.linkTo(problemDescription.bottom , margin = 10.dp)
                        start.linkTo(parent.start)
                    }
                )



                Text(
                    text = "Interested in Project ? View Team Details.",
                    color = BluePrimary,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.constrainAs(viewTeamDetails){
                        top.linkTo(projectCategory.bottom , margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth().background(LightTextColor).constrainAs(divider){
                top.linkTo(if(expanded) viewTeamDetails.bottom else problemDescription.bottom , margin = 6.dp)
            })

            // LIKE PROJECT TO SHOW SUPPORT
            IconButton(onClick = { },
                modifier = Modifier
                    .constrainAs(likeProject) {
                        top.linkTo(viewMoreOrLess.top )
                        bottom.linkTo(viewMoreOrLess.bottom)
                        start.linkTo(parent.end)
                    }
                    .size(26.dp)) {

                Icon(
                    painterResource(id = R.drawable.like) ,
                    contentDescription = "" , tint = LightWhite,)
            }

            TextButton(onClick = { /*TODO*/ } ,
                modifier = Modifier.constrainAs(viewMoreOrLess){

                    top.linkTo(if(expanded) viewTeamDetails.bottom else problemDescription.bottom , margin = 6.dp)
                    start.linkTo(parent.start)

                }) {

                Text(text = if(expanded) "View Less" else "View More" , color = LightWhite)
            }

            createHorizontalChain(likeProject , viewMoreOrLess , chainStyle = ChainStyle.Spread)

        }
    }
}
