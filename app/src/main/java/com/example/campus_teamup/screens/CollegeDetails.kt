package com.example.campus_teamup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.CustomDropdown
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White

@Composable
fun CollegeDetails() {

    val listOfCourse = listOf("B-Tech", "M-Tech", "BBA", "MBA", "BSc", "MSc")
    val listOfBranch = listOf("CSE", "IT", "ECE", "Civil", "Mechanical", "AIML", "IOT", "Other")
    val listOfGraduation =
        listOf("Before 2022", "2022", "2023", "2024", "2025", "2026", "2027", "2028")


    var selectedCourse by remember {
        mutableStateOf("Select Course")
    }

    var selectedBranch by remember {
        mutableStateOf("Select Branch")
    }
    var selectedGraduation by remember {
        mutableStateOf("Select Year")
    }

    ConstraintLayout() {

        val (userImage, userName, courseBranchYear , editOrUpdateBtn) = createRefs()


        Image(
            painterResource(id = R.drawable.profile),
            contentDescription = stringResource(id = R.string.user_profile_icon),
            modifier = Modifier
                .size(80.dp)
                .border(1.dp, White, RoundedCornerShape(40.dp))
                .constrainAs(userImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(text = "Ayush Porwal",
            color = White,
            modifier = Modifier.constrainAs(userName) {
                top.linkTo(userImage.bottom, margin = 10.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })






        Column(
            modifier = Modifier.constrainAs(courseBranchYear) {
                top.linkTo(userName.bottom, margin = 20.dp)
            },
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {


            CustomDropdown(
                options = listOfCourse,
                selectedOption = selectedCourse,
                onOptionSelected = { selectedCourse = it },
                backgroundColor = BackGroundColor,
                textColor = White,
                leadingIcon = R.drawable.college
            )
            CustomDropdown(

                options = listOfBranch,
                selectedOption = selectedBranch,
                onOptionSelected = { selectedBranch = it },
                backgroundColor = BackGroundColor,
                textColor = White,
                leadingIcon = R.drawable.college
            )

            CustomDropdown(

                options = listOfGraduation,
                selectedOption = selectedGraduation,
                onOptionSelected = { selectedGraduation = it },
                backgroundColor = BackGroundColor,
                textColor = White,
                leadingIcon = R.drawable.college
            )

        }

        OutlinedButton(
            onClick = { },
            modifier = Modifier.constrainAs(editOrUpdateBtn){
                top.linkTo(courseBranchYear.bottom , margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = BackGroundColor
            )
        ) {
            Text(text = "Edit" , color = White)
        }




    }
}