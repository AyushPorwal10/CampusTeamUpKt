package com.example.new_campus_teamup.userprofile.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.CheckEmptyFields
import com.example.new_campus_teamup.helper.ProgressIndicator
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.UserProfileViewModel

@Composable
fun CodingProfiles(
    userProfileViewModel: UserProfileViewModel,
    modifier: Modifier
) {
    val codingProfiles = remember { mutableStateListOf<String>() }
    val isLoading = userProfileViewModel.isLoading.collectAsState()

    val isEditing = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val fetchedCodingProfiles = userProfileViewModel.codingProfiles.collectAsState()


    LaunchedEffect(fetchedCodingProfiles.value) {
        val newProfiles = fetchedCodingProfiles.value.filterNot { it in codingProfiles }
        codingProfiles.addAll(newProfiles)
    }

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (profilesListRef, progressBarRef, addProfileBtnRef, editSaveBtnRef) = createRefs()

        LazyColumn(
            modifier = Modifier.constrainAs(profilesListRef) {
                top.linkTo(parent.top, margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
//                bottom.linkTo(addProfileBtnRef.top, margin = 16.dp)
            },
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(codingProfiles.size) { index ->
                ProfileFields(
                    profileLink = codingProfiles[index],
                    isReadOnly = !isEditing.value,
                    isEditing,
                    onDelete = {
                        codingProfiles.removeAt(index)
                    },
                    onProfileChange = { newProfile ->
                        codingProfiles[index] = newProfile
                    }
                )
            }
        }

        if (isLoading.value) {
            ProgressIndicator.showProgressBar(
                modifier = Modifier.constrainAs(progressBarRef) {
                    top.linkTo(profilesListRef.bottom, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                true
            )
        } else {
            OutlinedButton(
                onClick = {
                    // Allow user to add a new profile entry
                    isEditing.value = true
                    codingProfiles.add("")
                },
                modifier = Modifier.constrainAs(addProfileBtnRef) {
                    top.linkTo(profilesListRef.bottom, margin = 16.dp)
                },
                colors = ButtonDefaults.outlinedButtonColors(containerColor = BackGroundColor)
            ) {
                Text("Add", color = White)
            }

            OutlinedButton(
                onClick = {
                    isEditing.value = !isEditing.value
                    if (!isEditing.value) {

                        if (CheckEmptyFields.isCodingProfileUrlAreValid(codingProfiles.toList())) {
                            userProfileViewModel.saveCodingProfiles(codingProfiles.toList() , onSuccess = {
                                ToastHelper.showToast(context , "Updated Successfully")
                            }, onError = {
                                ToastHelper.showToast(context , "Something went wrong")
                            })

                        } else {
                            ToastHelper.showToast(context, "Please Enter Valid URL")
                            isEditing.value = true
                        }
                    }
                },
                modifier = Modifier.constrainAs(editSaveBtnRef) {
                    top.linkTo(profilesListRef.bottom, margin = 16.dp)
                },
                colors = ButtonDefaults.outlinedButtonColors(containerColor = BackGroundColor)
            ) {
                Text(text = if (isEditing.value) "Save" else "Edit", color = White)
            }
            createHorizontalChain(addProfileBtnRef, editSaveBtnRef, chainStyle = ChainStyle.Spread)
        }
    }
}

@Composable
fun ProfileFields(
    profileLink: String,
    isReadOnly: Boolean,
    isEditing: MutableState<Boolean>,
    onDelete: () -> Unit,
    onProfileChange: (String) -> Unit
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

        else -> R.drawable.coding
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = profileLink,
            onValueChange = onProfileChange,
            readOnly = !isEditing.value,
            modifier = Modifier
                .background(BackGroundColor)
                .fillMaxWidth(0.8f)
            ,
            shape = TextFieldStyle.defaultShape,
            placeholder = {
                Text(
                    stringResource(id = R.string.coding_profile_url),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
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

        IconButton(
            onClick = {
                isEditing.value = true
                onDelete()
            },
            modifier = Modifier.size(22.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = "Delete Profile",
                tint = White
            )
        }
    }
}

