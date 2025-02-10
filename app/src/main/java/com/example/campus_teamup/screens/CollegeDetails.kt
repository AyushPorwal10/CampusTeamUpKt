package com.example.campus_teamup.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.CustomDropdown
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.UserProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun CollegeDetails(
    modifier: Modifier = Modifier,
    userProfileViewModel: UserProfileViewModel = hiltViewModel()
) {

    val listOfCourse = listOf("B-Tech", "M-Tech", "BBA", "MBA", "BSc", "MSc")
    val listOfBranch = listOf("CSE", "IT", "ECE", "Civil", "Mechanical", "AIML", "IOT", "Other")
    val listOfGraduationYear =
        listOf("Before 2022", "2022", "2023", "2024", "2025", "2026", "2027", "2028")


    // if canEdit is false means user can only view , when click on save he will allowed to edit
    var canEdit by remember {
        mutableStateOf(false)
    }

    var selectedCourse by remember {
        mutableStateOf("Select Course")
    }

    var selectedBranch by remember {
        mutableStateOf("Select Branch")
    }
    var selectedGraduationYear by remember {
        mutableStateOf("Select Year")
    }

    var collegeName by remember {
        mutableStateOf("College")
    }

    var showProgressBar = remember{ mutableStateOf(false) }

    var coroutineScope = rememberCoroutineScope()


    // this is fetching of college details when user enter college details section


    LaunchedEffect(Unit){
        withContext(Dispatchers.Main){
            showProgressBar.value = true

            Log.d("CollegeDetails","Going to fetch college details")
            userProfileViewModel.getUserIdFromDataStore()

            val collegeDetails = userProfileViewModel.fetchCollegeDetails()
            selectedBranch = collegeDetails?.branch.toString()
            selectedCourse = collegeDetails?.course.toString()
            selectedGraduationYear = collegeDetails?.year.toString()
            //collegeName = collegeDetails.
            showProgressBar.value = false

            Log.d("CollegeDetails", "$selectedBranch , $selectedCourse , $selectedGraduationYear")
        }
    }

    ConstraintLayout(modifier = modifier.fillMaxSize()) {

        val (userImage, userName,progressBar, courseBranchYear, editOrUpdateBtn) = createRefs()

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
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(userName.bottom, margin = 20.dp)
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {


            OutlinedTextField(value = collegeName,
                onValueChange = { collegeName = it },
                colors = TextFieldStyle.myTextFieldColor(),
                shape = TextFieldStyle.defaultShape,
                maxLines = 2,
                readOnly = true, // this can't be edited
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.college), contentDescription = "",
                        modifier = Modifier.size(22.dp), tint = White
                    )
                }, modifier = modifier)


            CustomDropdown(
                canEdit,
                options = listOfCourse,
                selectedOption = selectedCourse,
                onOptionSelected = { selectedCourse = it },
                backgroundColor = BackGroundColor,
                textColor = White,
                leadingIcon = R.drawable.college
            )
            CustomDropdown(
                canEdit,
                options = listOfBranch,
                selectedOption = selectedBranch,
                onOptionSelected = { selectedBranch = it },
                backgroundColor = BackGroundColor,
                textColor = White,
                leadingIcon = R.drawable.college
            )

            CustomDropdown(
                canEdit,
                options = listOfGraduationYear,
                selectedOption = selectedGraduationYear,
                onOptionSelected = { selectedGraduationYear = it },
                backgroundColor = BackGroundColor,
                textColor = White,
                leadingIcon = R.drawable.college
            )

        }

        if(showProgressBar.value){
            ProgressIndicator.showProgressBar(
                Modifier.constrainAs(progressBar) {
                    top.linkTo(courseBranchYear.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                true)
        }else{
            OutlinedButton(
                onClick = {
                    canEdit = !canEdit
                    if (!canEdit) {
                        Log.d("CollegeDetails","Coroutine Scope Launched")



                        coroutineScope.launch(Dispatchers.IO) {
                            showProgressBar.value = true

                            Log.d("CollegeDetails","Going to fetch User id ")
                            userProfileViewModel.getUserIdFromDataStore()

                            userProfileViewModel.saveCollegeDetails(selectedGraduationYear, selectedBranch, selectedCourse)
                            Log.d("CollegeDetails","Done with saving college details")
                        }

                        showProgressBar.value = false
                    }


                },
                modifier = Modifier.constrainAs(editOrUpdateBtn) {
                    top.linkTo(courseBranchYear.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BackGroundColor
                )
            ) {
                Text(text = if (canEdit) "Save" else "Edit", color = White)
            }
        }


    }
}