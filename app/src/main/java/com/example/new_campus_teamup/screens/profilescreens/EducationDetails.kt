package com.example.new_campus_teamup.screens.profilescreens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.mydataclass.EducationDetails
import com.example.new_campus_teamup.ui.theme.IconColor
import com.example.new_campus_teamup.viewmodels.UserProfileViewModel


@Composable
fun EducationDetailsCard(userProfileViewModel: UserProfileViewModel) {
    var pressed by remember { mutableStateOf(false) }

    var showEditEducationDetailsCard = remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f
    )

    var educationDetails = userProfileViewModel.educationDetails.collectAsState()


    if (showEditEducationDetailsCard.value) {
        EditEducationalDetails(
            educationDetails = educationDetails,
            showEditEducationDetailsCard.value,
            onDismiss = {
                showEditEducationDetailsCard.value = false
            },
            userProfileViewModel
        )
    }

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
            containerColor = Color(0xFFCCC0FE)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // When user click on edit icon show user a dialog to edit education details
            DetailsCardHeading(
                true, onEditButtonClick = {
                    showEditEducationDetailsCard.value = true
                },
                "Education Details"
            )

            EducationDetailsField(educationDetails.value?.collegeName ?: "College")
            EducationDetailsField(educationDetails.value?.course ?: "Course")
            EducationDetailsField(educationDetails.value?.branch ?: "Branch")
            EducationDetailsField(educationDetails.value?.year ?: "Year")
        }
    }
}

@Composable
fun EducationDetailsField(collegeName: String?) {


    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.college),
            tint = IconColor,
            contentDescription = null,
            modifier = Modifier.padding(6.dp)
        )

        Text(collegeName + "", fontWeight = FontWeight.Medium)
    }
}

@Composable
fun EditEducationalDetails(
    educationDetails: State<EducationDetails?>,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    userProfileViewModel: UserProfileViewModel,
) {

    var selectedBranch = remember { mutableStateOf("Select branch") }
    var selectedCourse = remember { mutableStateOf("Select course") }
    var selectedPassingYear = remember { mutableStateOf("Select year") }
    var isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(educationDetails) {
        educationDetails.value?.let {
            selectedBranch.value = it.branch
            selectedCourse.value = it.course
            selectedPassingYear.value = it.year
        }
    }
    if (showDialog) {
        Dialog(onDismissRequest = {
            onDismiss()
        }) {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = Color(0xFFCCC0FE)
            ) {

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    DetailsCardHeading(
                        false , onEditButtonClick = {
                        }
                    , "Education Details")

                    EducationDetailsOptionsDropDown(
                        selectedBranch.value,
                        EducationDetailsOptions.branchOptionList,
                        onOptionSelected = {
                            selectedBranch.value = it
                        })
                    EducationDetailsOptionsDropDown(
                        selectedCourse.value,
                        EducationDetailsOptions.courseOptionList,
                        onOptionSelected = {
                            selectedCourse.value = it
                        }
                    )
                    EducationDetailsOptionsDropDown(
                        selectedPassingYear.value,
                        EducationDetailsOptions.yearOfPassingOptionList,
                        onOptionSelected = {
                            selectedPassingYear.value = it
                        }
                    )

                    Spacer(modifier = Modifier.size(30.dp))
                    CancelAndSaveButton(
                        isLoading,
                        onSave = {
                            userProfileViewModel.saveEducationDetails(
                                selectedPassingYear.value,
                                selectedBranch.value,
                                selectedCourse.value,
                                onSuccess = {

                                    onDismiss()
                                }
                            )
                        },
                        onCancel = {
                            onDismiss()
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationDetailsOptionsDropDown(
    selected: String,
    optionList: List<String>,
    onOptionSelected: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {


        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            placeholder = { Text(selected) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.college),
                    contentDescription = null
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            optionList.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
