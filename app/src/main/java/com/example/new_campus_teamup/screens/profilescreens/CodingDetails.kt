package com.example.new_campus_teamup.screens.profilescreens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.ui.theme.IconColor
import com.example.new_campus_teamup.viewmodels.UserProfileViewModel


@Composable
fun CodingProfilesCard(userProfileViewModel: UserProfileViewModel) {
    var pressed by remember { mutableStateOf(false) }

    val codingProfiles = userProfileViewModel.codingProfiles.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

    Log.d("Learning","In Composable Fetched coding profile size is ${codingProfiles.value.size}")

    if (showDialog.value) {
        EditCodingProfile(codingProfiles, showDialog.value, onDismiss = {
            showDialog.value = false
        }, userProfileViewModel)
    }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f
    )

    Card(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false

                    }
                )
            }
            .fillMaxWidth(0.9f)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF7EEBBE)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {


        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            DetailsCardHeading(true , onEditButtonClick = {
                showDialog.value = true
            } , "Coding Profiles")

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 70.dp ,max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(codingProfiles.value) { profileUrl ->
                    ShowCodingProfiles(profileUrl)
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

        }
    }
}

@Composable
fun ShowCodingProfiles(profileUrl: String) {

    val platformNameAndIcon = CheckProfileLink.getPlatformNameAndIcon(profileUrl)
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(platformNameAndIcon.platformIcon),
                    tint = IconColor,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 6.dp).size(38.dp)
                )

                Text(
                    text = platformNameAndIcon.platformName,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = Color(0xFF6E66D3),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Text(
                    "Visit",
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.padding(start = 10.dp, end = 2.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.college),
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 2.dp, end = 10.dp)
                        .size(26.dp)
                )
            }

        }
    }


}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditCodingProfile(
    userCodingProfileList: State<List<String>>,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    userProfileViewModel: UserProfileViewModel
) {

    val context = LocalContext.current
    val currentListOfCodingProfiles = remember { mutableStateListOf<String>() }
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(userCodingProfileList.value) {
        currentListOfCodingProfiles.clear()
        currentListOfCodingProfiles.addAll(userCodingProfileList.value)
    }

    if (showDialog) {
        Dialog(onDismissRequest = {
            onDismiss()
        }) {


            Surface(
                shape = RoundedCornerShape(22.dp),
                color = Color(0xFFEDF9FE)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DetailsCardHeading(false , onEditButtonClick = {

                    } , "Coding Profiles")


                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        itemsIndexed(currentListOfCodingProfiles) { index, codingProfileUrl ->
                            AddCodingProfile(codingProfileUrl, onDelete = { deleteProfile ->
                                currentListOfCodingProfiles.remove(deleteProfile)
                            }, onEdit = { editingUrl ->
                                currentListOfCodingProfiles[index] = editingUrl
                            })
                        }
                        item {
                            AddNewCodingProfile(onAddButtonClick = {
                                // allowing user to add only 5 coding profiles as of now
                                if (currentListOfCodingProfiles.size > 4) {
                                    ToastHelper.showToast(context, "You can add up to 5 profiles.")
                                } else {
                                    currentListOfCodingProfiles.add("")
                                }
                            })
                        }

                        item {
                            CancelAndSaveButton(isLoading, onCancel = {
                                onDismiss()
                            }, onSave = {
                                val isCodingProfileIsBlank =
                                    currentListOfCodingProfiles.any { it.isBlank() }

                                if (isCodingProfileIsBlank) {
                                    isLoading.value = false
                                    ToastHelper.showToast(context, "Coding Profile can't be empty.")
                                } else {
                                    userProfileViewModel.saveCodingProfiles(
                                        currentListOfCodingProfiles,
                                        onSuccess = {
                                            isLoading.value = false
                                            onDismiss()
                                        },
                                        onError = {
                                            ToastHelper.showToast(
                                                context,
                                                "Something went wrong\nPlease try again later"
                                            )
                                        })
                                }
                            })
                            Spacer(modifier = Modifier.size(10.dp))
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun AddNewCodingProfile(onAddButtonClick: () -> Unit) {
    OutlinedButton(onClick = {
        onAddButtonClick()
    }) {
        Text("Add", fontWeight = FontWeight.Medium, color = Color.Black)
    }
}

@Composable
fun AddCodingProfile(url: String, onDelete: (String) -> Unit, onEdit: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = url,
            onValueChange = {
                onEdit(it)
            },
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            modifier = Modifier.weight(1f),
            label = { Text("Profile url", style = MaterialTheme.typography.bodySmall) },
            leadingIcon = {
                IconButton(onClick = {

                }) {
                    Icon(
                        painter = painterResource(
                            if (url.contains(
                                    "leetcode",
                                    ignoreCase = true
                                )
                            ) R.drawable.leetcode else R.drawable.coding
                        ),
                        contentDescription = "Confirm"
                    )
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = {
            onDelete(url)
        }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Skill",
                tint = Color.Red
            )
        }
    }
}
