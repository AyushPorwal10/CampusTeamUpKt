package com.example.campus_teamup.screens

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campus_teamup.R
import com.example.campus_teamup.ui.theme.BackGroundColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.example.campus_teamup.helper.CustomDropdown
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.myactivities.CreatePost
import com.example.campus_teamup.mydataclass.RoleDetails
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.HomeScreenViewModel
import com.example.campus_teamup.viewmodels.TeamDetailsViewModel
import com.example.campus_teamup.viewmodels.UserProfileViewModel


@Composable
fun InputField(
    teamDetailsViewModel: TeamDetailsViewModel,
    suggestionList: List<String>,
    searchingText: String,
    modifier: Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = searchingText,
            onValueChange = {
                teamDetailsViewModel.onSearchTextChange(it)
            },
            modifier = Modifier
                .background(BackGroundColor)
                .fillMaxWidth(0.8f),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "",
                    tint = BorderColor
                )
            },
            shape = TextFieldStyle.defaultShape,
            placeholder = { Text("Username", maxLines = 2) },
            colors = TextFieldStyle.myTextFieldColor(),
        )
        if (suggestionList.isNotEmpty() && searchingText.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 10.dp)
            ) {
                items(suggestionList) { suggestedUserName ->
                    Text(text = suggestedUserName, modifier = Modifier.clickable {
                    }, color = White, style = MaterialTheme.typography.titleSmall)
                }
            }
        }

    }


}


//@Composable
//fun Wait(
//    userProfileViewModel: UserProfileViewModel,
//    modifier: Modifier
//) {
//
//    val tag: String = "CollegeDetails"
//    val listOfCourse = listOf("B-Tech", "M-Tech", "BBA", "MBA", "BSc", "MSc")
//    val listOfBranch = listOf("CSE", "IT", "ECE", "Civil", "Mechanical", "AIML", "IOT", "Other")
//    val listOfGraduationYear =
//        listOf("Before 2022", "2022", "2023", "2024", "2025", "2026", "2027", "2028")
//
//
//
//
//    val isLoading = userProfileViewModel.isLoading.collectAsState()
//    val collegeDetails = userProfileViewModel.collegeDetails.collectAsState().value
//
//    // if canEdit is false means user can only view , when click on save he will allowed to edit
//    var isEditing by remember {
//        mutableStateOf(false)
//    }
//
//    var selectedCourse by remember {
//        mutableStateOf("Select Course")
//    }
//    var selectedBranch by remember {
//        mutableStateOf("Select Branch")
//    }
//    var selectedGraduationYear by remember {
//        mutableStateOf("Select Year")
//    }
//    var collegeName by remember {
//        mutableStateOf("College")
//    }
//    var userName by remember {
//        mutableStateOf("Your Name")
//    }
//    var selectedImageFromDevice by remember { mutableStateOf<Uri?>(null) }
//    val context = LocalContext.current
//
//
//
//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        if (uri != null) {
//            Log.d(tag, "Image selected $uri")
//            selectedImageFromDevice = uri
//        } else {
//            ToastHelper.showToast(context, "No Image Selected")
//        }
//    }
//
//
//    var downloadedImageUrl: String? = null
//
//    LaunchedEffect(collegeDetails) {
//        collegeDetails.let {
//            if (it != null) {
//                selectedBranch = it.branch
//            }
//            if (it != null) {
//                selectedCourse = it.course
//            }
//            if (it != null) {
//                selectedGraduationYear = it.year
//            }
//            if (it != null) {
//                collegeName = it.collegeName
//            }
//            if (it != null) {
//                userName = it.userName
//            }
//            if(it != null){
//                downloadedImageUrl = it.userImageUrl
//            }
//            else{
//                Log.d("Image","downLoadImageUrl is null")
//            }
//        }
//    }
//    Log.d("Image", "$selectedBranch , $selectedCourse , $selectedGraduationYear , $downloadedImageUrl")
//
//
//
//    ConstraintLayout(modifier = modifier.fillMaxSize()) {
//
//        val (userImage, editPhotoButton, progressBar, courseBranchYear, editOrUpdateBtn) = createRefs()
//
//        AsyncImage(
//            model = selectedImageFromDevice ?: R.drawable.profile,
//            contentDescription = "User Profile",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(80.dp)
//                .clip(CircleShape)
//                .border(1.dp, White, CircleShape)
//                .constrainAs(userImage) {
//                    top.linkTo(parent.top)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                })
//
//        if (isEditing) {
//            IconButton(onClick = {
//                imagePickerLauncher.launch("image/*")
//            }, modifier = Modifier
//                .constrainAs(editPhotoButton) {
//                    top.linkTo(parent.top)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                }
//                .size(80.dp)) {
//                Icon(
//                    painter = painterResource(id = R.drawable.change_photo),
//                    contentDescription = null,
//                    Modifier.size(20.dp)
//                )
//            }
//        }
//
//        Column(
//            modifier = Modifier.constrainAs(courseBranchYear) {
//                start.linkTo(parent.start)
//                end.linkTo(parent.end)
//                top.linkTo(userImage.bottom, margin = 40.dp)
//            },
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//
//
//            OutlinedTextField(
//                value = collegeName,
//                onValueChange = { collegeName = it },
//                colors = TextFieldStyle.myTextFieldColor(),
//                shape = TextFieldStyle.defaultShape,
//                maxLines = 2,
//                readOnly = true, // this can't be edited
//                leadingIcon = {
//                    Icon(
//                        painterResource(id = R.drawable.college), contentDescription = "",
//                        modifier = Modifier.size(22.dp), tint = White
//                    )
//                }, modifier = Modifier.fillMaxWidth(0.85f)
//            )
//
//
//            CustomDropdown(
//                isEditing,
//                options = listOfCourse,
//                selectedOption = selectedCourse,
//                onOptionSelected = { selectedCourse = it },
//                backgroundColor = BackGroundColor,
//                textColor = White,
//                leadingIcon = R.drawable.college
//            )
//            CustomDropdown(
//                isEditing,
//                options = listOfBranch,
//                selectedOption = selectedBranch,
//                onOptionSelected = { selectedBranch = it },
//                backgroundColor = BackGroundColor,
//                textColor = White,
//                leadingIcon = R.drawable.college
//            )
//
//            CustomDropdown(
//                isEditing,
//                options = listOfGraduationYear,
//                selectedOption = selectedGraduationYear,
//                onOptionSelected = { selectedGraduationYear = it },
//                backgroundColor = BackGroundColor,
//                textColor = White,
//                leadingIcon = R.drawable.college
//            )
//
//        }
//
//        if (isLoading.value) {
//            ProgressIndicator.showProgressBar(
//                Modifier.constrainAs(progressBar) {
//                    top.linkTo(courseBranchYear.bottom, margin = 20.dp)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                },
//                true
//            )
//        } else {
//            OutlinedButton(
//                onClick = {
//                    isEditing = !isEditing
//                    if (!isEditing) {
//
//
//                        Log.d("CollegeDetails", "Coroutine Scope Launched")
//
//
//                        userProfileViewModel.uploadUserImageToStorage(
//                            selectedImageFromDevice!!,
//                            onResult = { url ->
//
//                                downloadedImageUrl = url ?: ""
//
//                                Log.d("CollegeDetails", " Download url is  $downloadedImageUrl")
//                                userProfileViewModel.saveCollegeDetails(
//                                    downloadedImageUrl!!,
//                                    selectedGraduationYear,
//                                    selectedBranch,
//                                    selectedCourse
//                                )
//
//                                Log.d("CollegeDetails", "Done with saving college details")
//
//                            })
//
//
//                    }
//
//
//                },
//                modifier = Modifier.constrainAs(editOrUpdateBtn) {
//                    top.linkTo(courseBranchYear.bottom, margin = 20.dp)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = BackGroundColor
//                )
//            ) {
//                Text(text = if (isEditing) "Save" else "Edit", color = White)
//            }
//        }
//
//
//    }
//}