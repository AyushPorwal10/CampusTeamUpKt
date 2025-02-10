package com.example.campus_teamup.screens

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.UserProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CodingProfiles(
    modifier: Modifier,
    userProfileViewModel: UserProfileViewModel = hiltViewModel()
) {

    var listOfCodingProfile by remember {
        mutableStateOf(mutableListOf<String>())
    }

    var isReadOnly = remember {
        mutableStateOf(true)
    }
    val coroutineScope = rememberCoroutineScope()



    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (listOfProfiles, addProfilesBtn, editOrUpdateBtn) = createRefs()

        LazyColumn(modifier = Modifier.constrainAs(listOfProfiles) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(addProfilesBtn.top, margin = 16.dp)
        }, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(listOfCodingProfile.size) { index ->

                ProfileFields(
                    profileLink = listOfCodingProfile[index],
                    onDelete = {
                        listOfCodingProfile = listOfCodingProfile.toMutableList().apply {
                            removeAt(index)
                        }
                    },
                    onProfileChange = { newProfile ->
                        listOfCodingProfile = listOfCodingProfile.toMutableList().apply {
                            this[index] = newProfile
                        }
                    }
                )
            }


        }






        OutlinedButton(
            onClick = {
                isReadOnly.value = true // means user wants to add more profiles so allow to save

                listOfCodingProfile = listOfCodingProfile.toMutableList().apply {
                    add("")
                }
            },
            modifier = Modifier.constrainAs(addProfilesBtn) {
                top.linkTo(listOfProfiles.bottom, margin = 16.dp)

            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = BackGroundColor)
        ) {
            Text("Add ", color = White)
        }

        OutlinedButton(
            onClick = {
                isReadOnly.value = !isReadOnly.value
                if (isReadOnly.value) { // this means fields are set to read only save it to db
                    coroutineScope.launch(Dispatchers.IO) {
                        Log.d("CodingProfiles", "Going to save coding profiles")
                        userProfileViewModel.saveCodingProfiles(listOfCodingProfile)
                    }
                }

            },
            modifier = Modifier.constrainAs(editOrUpdateBtn) {
                top.linkTo(listOfProfiles.bottom, margin = 16.dp)
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = BackGroundColor)
        ) {
            Text(text = if (isReadOnly.value) "Edit" else "Save", color = White)
        }

        createHorizontalChain(addProfilesBtn, editOrUpdateBtn, chainStyle = ChainStyle.Spread)

    }
}

@Composable
fun ProfileFields(
    profileLink: String,
    onDelete: () -> Unit,
    onProfileChange: (String) -> Unit
) {


    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = profileLink,
            onValueChange = onProfileChange,
            modifier = Modifier
                .background(BackGroundColor)
                .fillMaxWidth(0.8f),          //0.75
            shape = TextFieldStyle.defaultShape,
            placeholder = {
                Text(
                    "Enter URL (Leetcode , Github ...",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.profile), contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            },
            colors = TextFieldStyle.myTextFieldColor(),
        )

        IconButton(onClick = {
            onDelete()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.password),
                contentDescription = null, tint = White, modifier = Modifier.size(22.dp)
            )

        }
    }

}
