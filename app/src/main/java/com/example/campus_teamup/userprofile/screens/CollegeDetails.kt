package com.example.campus_teamup.userprofile.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.CustomDropdown
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.UserProfileViewModel

@Composable
fun CollegeDetails(
    userProfileViewModel: UserProfileViewModel,
    modifier: Modifier
) {

    val tag: String = "CollegeDetails"
    val listOfCourse = listOf("B-Tech", "M-Tech", "BBA", "MBA", "BSc", "MSc")
    val listOfBranch = listOf("CSE", "IT", "ECE", "Civil", "Mechanical", "AIML", "IOT", "Other")
    val listOfGraduationYear =
        listOf("Before 2022", "2022", "2023", "2024", "2025", "2026", "2027", "2028")

    val currentUserImage = userProfileViewModel.currentUserImage.collectAsState()
    Log.d("CollegeDetails","Current user image is $currentUserImage.va")

    val isLoading = userProfileViewModel.isLoading.collectAsState()
    val collegeDetails = userProfileViewModel.collegeDetails.collectAsState().value

    // if canEdit is false means user can only view , when click on save he will allowed to edit
    var isEditing by remember {
        mutableStateOf(false)
    }

    var selectedCourse by remember {
        mutableStateOf("other")
    }
    var selectedBranch by remember {
        mutableStateOf("other")
    }
    var selectedGraduationYear by remember {
        mutableStateOf("other")
    }
    var collegeName by remember {
        mutableStateOf("College")
    }
    var userName by remember {
        mutableStateOf("Your Name")
    }
    var selectedImageFromDevice by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            Log.d(tag, "Image selected $uri")
            selectedImageFromDevice = uri
        } else {
            ToastHelper.showToast(context, "No Image Selected")
        }
    }


    var downloadedImageUrl: String? = null

    LaunchedEffect(collegeDetails) {
        collegeDetails?.let {
            selectedBranch = it.branch
            selectedCourse = it.course
            selectedGraduationYear = it.year
            collegeName = it.collegeName
            userName = it.userName
            downloadedImageUrl = it.userImageUrl
            selectedImageFromDevice = it.userImageUrl.toUri()
            Log.d("Image", "Updated UI with image: $downloadedImageUrl")
        }
    }
    Log.d("Image", "$selectedBranch , $selectedCourse , $selectedGraduationYear , $downloadedImageUrl")


    ConstraintLayout(modifier = modifier.fillMaxSize()) {

        val (userImage, editPhotoButton, progressBar, courseBranchYear, editOrUpdateBtn) = createRefs()

        AsyncImage(
            model = selectedImageFromDevice ?: currentUserImage.value.isNotEmpty(),
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

        if (isEditing) {
            IconButton(onClick = {
                imagePickerLauncher.launch("image/*")
            }, modifier = Modifier
                .constrainAs(editPhotoButton) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(80.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.change_photo),
                    contentDescription = null,
                    Modifier.size(20.dp)
                )
            }
        }

        Column(
            modifier = Modifier.constrainAs(courseBranchYear) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(userImage.bottom, margin = 40.dp)
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {


            OutlinedTextField(
                value = collegeName,
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
                }, modifier = Modifier.fillMaxWidth(0.85f)
            )


            CustomDropdown(
                isEditing,
                options = listOfCourse,
                selectedOption = selectedCourse,
                onOptionSelected = { selectedCourse = it },
                backgroundColor = BackGroundColor,
                textColor = White,
                leadingIcon = R.drawable.college
            )
            CustomDropdown(
                isEditing,
                options = listOfBranch,
                selectedOption = selectedBranch,
                onOptionSelected = { selectedBranch = it },
                backgroundColor = BackGroundColor,
                textColor = White,
                leadingIcon = R.drawable.college
            )

            CustomDropdown(
                isEditing,
                options = listOfGraduationYear,
                selectedOption = selectedGraduationYear,
                onOptionSelected = { selectedGraduationYear = it },
                backgroundColor = BackGroundColor,
                textColor = White,
                leadingIcon = R.drawable.college
            )

        }

        if (isLoading.value) {
            ProgressIndicator.showProgressBar(
                Modifier.constrainAs(progressBar) {
                    top.linkTo(courseBranchYear.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                true
            )
        } else {
            OutlinedButton(
                onClick = {
                    isEditing = !isEditing
                    if (!isEditing) {

                        Log.d("CollegeDetails", "Coroutine Scope Launched")



                        if (selectedImageFromDevice != null) {
                            userProfileViewModel.uploadUserImageToStorage(
                                selectedImageFromDevice!!,
                                onResult = { url ->
                                    downloadedImageUrl = url ?: downloadedImageUrl

                                    Log.d("CollegeDetails", "Download URL is $downloadedImageUrl")

                                        userProfileViewModel.saveCollegeDetails(
                                            downloadedImageUrl ?: currentUserImage.value,
                                            selectedGraduationYear,
                                            selectedBranch,
                                            selectedCourse
                                        )


                                    Log.d("CollegeDetails", "Done with saving college details")
                                })
                        }
                        else
                            ToastHelper.showToast(context , "Please add your profile image")
                    }


                },
                modifier = Modifier.constrainAs(editOrUpdateBtn) {
                    top.linkTo(courseBranchYear.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BackGroundColor
                ),
            ) {
                Log.d("CollegeDetails","$selectedImageFromDevice <-")
                Text(text = if (isEditing) "Save" else "Edit", color = White)
            }
        }


    }
}