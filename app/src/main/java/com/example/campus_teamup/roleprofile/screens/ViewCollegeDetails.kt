package com.example.campus_teamup.roleprofile.screens

import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.roleprofile.ViewProfileViewModel
import com.example.campus_teamup.ui.theme.White

@Composable
fun ViewCollegeDetails(modifier: Modifier , viewProfileViewModel: ViewProfileViewModel) {


    val collegeDetails = viewProfileViewModel.collegeDetails.collectAsState()

    ConstraintLayout(modifier = modifier.fillMaxSize()) {

        val (userImage,  courseBranchYear) = createRefs()

        AsyncImage(
            model = collegeDetails.value?.userImageUrl,
            contentDescription = "User Profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(1.dp, White, CircleShape)
                .constrainAs(userImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })



        Column(
            modifier = Modifier.constrainAs(courseBranchYear) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(userImage.bottom, margin = 40.dp)
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {



            ShowDetails(details = collegeDetails.value?.collegeName, icon = R.drawable.college)
            ShowDetails(details = "Course : " + collegeDetails.value?.course, icon = R.drawable.college)
            ShowDetails(details = "Branch : " + collegeDetails.value?.branch, icon = R.drawable.college)
            ShowDetails(details = "Graduation year : " + collegeDetails.value?.year, icon = R.drawable.college)

        }
    }
}

@Composable
fun ShowDetails(details : String? , icon : Int){
    OutlinedTextField(
        value = details?: "",
        onValueChange = { },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 2,
        readOnly = true,
        leadingIcon = {
            Icon(
                painterResource(id = icon), contentDescription = "",
                modifier = Modifier.size(22.dp), tint = White
            )
        }, modifier = Modifier.fillMaxWidth(0.85f)
    )
}