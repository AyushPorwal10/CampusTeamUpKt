package com.example.new_campus_teamup.roleprofile.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.CheckEmptyFields
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.viewmodels.ViewProfileViewModel
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.White


@Composable
fun ViewCodingProfiles(
    modifier: Modifier,
    viewProfileViewModel: ViewProfileViewModel
) {
    val codingProfiles by viewProfileViewModel.codingProfilesDetails.collectAsState()

    val context = LocalContext.current

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
                CodingProfileCard(profileLink, context)
            }
        }
    }
}




@Composable
fun CodingProfileCard(
    profileLink: String,
    context: Context
) {

    Log.d("CodingProfiles","Profile Link is $profileLink")

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


    val platformName = when {
        profileLink.contains("leetcode", ignoreCase = true) -> "Leetcode"
        profileLink.contains("github", ignoreCase = true) -> "Github"
        profileLink.contains("linkedin", ignoreCase = true) -> "LinkedIn"
        profileLink.contains("codechef", ignoreCase = true) -> "CodeChef"
        profileLink.contains("codeforces", ignoreCase = true) -> "Codeforces"
        profileLink.contains("gfg", ignoreCase = true) || profileLink.contains(
            "geeksforgeeks",
            ignoreCase = true
        ) -> "GeeksforGeeks"
        else -> "Coding Profile"  // Fallback icon if none of the above match
    }



    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = BorderColor
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Gray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconResource),
                    contentDescription = profileLink,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Row(modifier = Modifier.weight(1f)) {
                Text(
                    text = platformName,
                    color = White ,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "View",
                style = MaterialTheme.typography.titleMedium,
                color = LightTextColor,
                modifier = Modifier.align(Alignment.CenterVertically)
                    .clickable {
                        if(CheckEmptyFields.isValidHttpsUrl(profileLink)){
                            val browseLink = Intent(Intent.ACTION_VIEW , Uri.parse(profileLink))
                            context.startActivity(browseLink)
                        }
                        else {
                            ToastHelper.showToast(context , "No Profile found.")
                        }

                    }
            )
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
