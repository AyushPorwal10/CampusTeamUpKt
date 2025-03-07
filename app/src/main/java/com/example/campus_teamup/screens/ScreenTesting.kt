package com.example.campus_teamup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.campus_teamup.R
import com.example.campus_teamup.roleprofile.screens.CodingProfilesBtn
import com.example.campus_teamup.roleprofile.screens.CollegeDetailsBtn
import com.example.campus_teamup.roleprofile.screens.SkillSectionBtn
import com.example.campus_teamup.roleprofile.screens.ViewCodingProfiles
import com.example.campus_teamup.roleprofile.screens.ViewCollegeDetails
import com.example.campus_teamup.roleprofile.screens.ViewSkills
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.ViewProfileViewModel



//@Composable
//fun Testing() {
//    AsyncImage(
//        model ="https://firebasestorage.googleapis.com/v0/b/learnsign-in.appspot.com/o/user_images%2Fashokporwal723%2Fprofile.jpg?alt=media&token=933febcb-30d9-4993-87b3-feb9ad7acda1" ,
//        contentDescription = "User Profile",
//        contentScale = ContentScale.Crop,
//        modifier = Modifier
//            .size(80.dp)
//            .clip(CircleShape)
//            .border(1.dp, White, CircleShape)
//            )
//}