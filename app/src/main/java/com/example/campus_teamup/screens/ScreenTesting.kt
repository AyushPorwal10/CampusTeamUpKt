package com.example.campus_teamup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Preview
@Composable
fun CollegeDetailsRedesign() {

    val listOfCourse = listOf("B-Tech" , "M-Tech" , "BBA" , "MBA" , "BSc" , "MSc")
    val listOfBranch = listOf("CSE" ,"IT" , "ECE" , "Civil" , "Mechanical" , "AIML" , "IOT" , "Other")


    var selectedBranch by remember {
        mutableStateOf("")
    }
    var selectedCourse by remember {
        mutableStateOf("Select Course")
    }

    ConstraintLayout(Modifier.fillMaxSize()) {   // for testing fillMaxWidth

        val (userImage , userName , courseTaken, branchTaken , graduationYear) = createRefs()


        Image(
            painterResource(id = R.drawable.profile) ,
            contentDescription = stringResource(id = R.string.user_profile_icon),
            modifier = Modifier
                .size(60.dp)
                .border(1.dp, White, RoundedCornerShape(30.dp))
                .constrainAs(userImage) {
                    top.linkTo(parent.top)
                }
        )

        Text(text = "Ayush Porwal" ,
            color = White,
            modifier = Modifier.constrainAs(userName){
                top.linkTo(userImage.bottom , margin= 10.dp)
            })







        Box(modifier = Modifier.constrainAs(courseTaken){
            top.linkTo(userName.bottom , margin = 20.dp)
        }){

            CustomDropdown(
                options = listOfCourse,
                selectedOption = selectedCourse,
                onOptionSelected = {selectedCourse = it},
                backgroundColor = BackGroundColor,
                textColor = White,
                leadingIcon = R.drawable.college
            )

        }




    }
}
