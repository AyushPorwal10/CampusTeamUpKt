package com.example.campus_teamup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White


@OptIn(ExperimentalMaterial3Api::class)

@Composable

fun UserProfiles() {

    var isClicked by remember { mutableStateOf(false) }

    var selectedLayout by remember {
        mutableStateOf("collegeDetails")
    }


    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = White
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = BackGroundColor,
                navigationIconContentColor = White
            ),
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.browseback),
                        contentDescription = null,
                        tint = White
                    )
                }
            }
        )
    }) {paddingValues ->

        ConstraintLayout(modifier = Modifier
            .background(BackGroundColor)
            .fillMaxSize()
            .padding(paddingValues)
            ) {

            val (topAppBar ,divider, codingProfileBtn , collegeDetailsBtn , skillSection ,codingProfilesArea , collegeDetailsArea , skillSectionArea ) = createRefs()



            HorizontalDivider(modifier = Modifier
                .fillMaxWidth()
                .background(BorderColor)
                .constrainAs(divider) {

                })

            // CODING PROFILE BUTTON and COLLEGE DETAILS BUTTON

            CollegeDetailsBtn(
                modifier = Modifier
                    .constrainAs(collegeDetailsBtn) {
                        top.linkTo(topAppBar.bottom, margin = 20.dp)
                    }
                    .padding(all = 0.dp)
                    .height(34.dp), selectedLayout , onClick = {
                    isClicked = !isClicked
                    selectedLayout = "collegeDetails"
                }
            )

            CodingProfilesBtn(
                modifier = Modifier
                    .constrainAs(codingProfileBtn) {
                        top.linkTo(topAppBar.bottom, margin = 20.dp)
                    }
                    .padding(all = 0.dp)
                    .height(34.dp), selectedLayout , onClick = {
                    isClicked = !isClicked
                    selectedLayout = "codingProfiles"
                }
            )
            SkillSectionBtn(
                modifier = Modifier
                    .constrainAs(skillSection) {
                        top.linkTo(topAppBar.bottom, margin = 20.dp)
                    }
                    .padding(all = 0.dp)
                    .height(34.dp), selectedLayout , onClick = {
                    isClicked = !isClicked
                    selectedLayout = "skillSection"
                }
            )


            createHorizontalChain(collegeDetailsBtn , codingProfileBtn ,skillSection, chainStyle = ChainStyle.Spread)






                if(selectedLayout == "collegeDetails"){
                    CollegeDetails(modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize()
                        .constrainAs(collegeDetailsArea) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(collegeDetailsBtn.bottom, margin = 20.dp)
                        })
                }

                else if(selectedLayout == "codingProfiles"){
                    CodingProfiles(modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize()
                        .constrainAs(codingProfilesArea) {

                            top.linkTo(collegeDetailsBtn.bottom, margin = 20.dp)
                        })
                }
               else {
                    SkillSection(
                        modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize()
                        .constrainAs(skillSectionArea) {

                            top.linkTo(collegeDetailsBtn.bottom, margin = 20.dp) }
                    )
                }



        }
    }


}

@Composable
fun CodingProfilesBtn(modifier: Modifier, selectedLayout: String, onClick: () -> Unit) {

    OutlinedButton(onClick =onClick ,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedLayout == "codingProfiles") White else BackGroundColor
        )
    ) {
        Text(
            text = stringResource(id = R.string.coding),
            color = if(selectedLayout == "codingProfiles" ) Black else White
        )
    }
}

@Composable
fun CollegeDetailsBtn(modifier: Modifier, selectedLayout : String, onClick : () -> Unit) {
    OutlinedButton(onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedLayout == "collegeDetails") White else BackGroundColor
        )

    ) {
        Text(
            text = stringResource(id = R.string.college),
            color = if(selectedLayout == "collegeDetails" ) Black else White
        )
    }
}

@Composable
fun SkillSectionBtn(modifier: Modifier, selectedLayout : String, onClick : () -> Unit) {
    OutlinedButton(onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedLayout == "skillSection") White else BackGroundColor
        )

    ) {
        Text(
            text = stringResource(id = R.string.skills),
            color = if(selectedLayout == "skillSection" ) Black else White
        )
    }
}

