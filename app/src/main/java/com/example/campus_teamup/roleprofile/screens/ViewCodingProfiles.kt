package com.example.campus_teamup.roleprofile.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.CheckEmptyFields
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.roleprofile.ViewProfileViewModel
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.UserProfileViewModel


@Composable
fun ViewCodingProfiles(
    modifier: Modifier,
    viewProfileViewModel: ViewProfileViewModel
) {
    val codingProfiles by viewProfileViewModel.codingProfilesDetails.collectAsState()


    // here you can manage modifier
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (profilesListRef, progressBarRef, addProfileBtnRef, editSaveBtnRef) = createRefs()

        LazyColumn(
            modifier = modifier.constrainAs(profilesListRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(addProfileBtnRef.top, margin = 16.dp)
            },
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            items(codingProfiles){profileLink->
                ProfileFields(profileLink)
            }
        }
    }
}

@Composable
fun ProfileFields(
    profileLink: String,
) {

    val iconResource = when {
        profileLink.contains("leetcode", ignoreCase = true) -> R.drawable.leetcode
        profileLink.contains("github", ignoreCase = true) -> R.drawable.github
        profileLink.contains("linkedin", ignoreCase = true) -> R.drawable.linkedin
        profileLink.contains("codechef", ignoreCase = true) -> R.drawable.codechef
        profileLink.contains("codeforces", ignoreCase = true) -> R.drawable.codeforces
        profileLink.contains("gfg", ignoreCase = true) || profileLink.contains(
            "geeksforgeeks",
            ignoreCase = true
        ) -> R.drawable.gfg

        else -> R.drawable.coding  // Fallback icon if none of the above match
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = profileLink,
            onValueChange = {},
            readOnly =true,
            modifier = Modifier
                .background(BackGroundColor)
                .fillMaxWidth(0.9f),
            shape = TextFieldStyle.defaultShape,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = iconResource),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified
                )
            },
            colors = TextFieldStyle.myTextFieldColor(),
        )
    }
}
