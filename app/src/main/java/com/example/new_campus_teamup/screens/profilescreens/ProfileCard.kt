package com.example.new_campus_teamup.screens.profilescreens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.UserProfileViewModel


@Composable
fun ProfileCard(userProfileViewModel: UserProfileViewModel) {


    var pressed by remember { mutableStateOf(false) }
    val fetchedUserProfileImage = userProfileViewModel.currentUserImage.collectAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f
    )
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    if(showDialog.value){
        AddImageCard(showDialog.value , onDismiss = {
            showDialog.value = false }, userProfileViewModel)
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
            containerColor = Color(0xFFbce3f6)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopEnd).clickable {
                    showDialog.value = true
                }
            )

            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {

                AsyncImage(
                    model = fetchedUserProfileImage.value.ifBlank { R.drawable.profile },
                    contentDescription = "User Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(1.dp, White, CircleShape)
                )

                Text(
                    "Ayush Porwal",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "ayushporwal1010@gmail.com",
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}


@Composable
fun AddImageCard(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    userProfileViewModel: UserProfileViewModel
) {
    val context = LocalContext.current

    val isLoading = remember { mutableStateOf(false) }

    val selectedImageFromUser = remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedImageFromUser.value = uri
        } else {
            ToastHelper.showToast(context, "No image selected")
        }
    }

    if (showDialog) {

        Dialog(onDismissRequest = {
            onDismiss()
        }) {
            Surface(
                color = Color(0xFFbce3f6),
                shape = RoundedCornerShape(22.dp)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(30.dp)
                ) {
                    AsyncImage(
                        model = selectedImageFromUser.value ?: R.drawable.profile,
                        contentDescription = "User Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .border(1.dp, White, CircleShape).clickable {
                                imagePickerLauncher.launch("image/*")
                            }
                    )

                    if(isLoading.value){
                        CircularProgressIndicator()
                    }
                    else {
                        TextButton(onClick = {
                            isLoading.value = true
                            if(selectedImageFromUser.value == null){
                                onDismiss()
                            }
                            else {
                                userProfileViewModel.saveImageUrl(selectedImageFromUser.value!!, onSuccess = {
                                    ToastHelper.showToast(context , "Profile photo updated.")
                                    isLoading.value = false
                                    onDismiss()
                                })
                            }

                        }) {
                            Text("Save")
                        }
                    }


                }
            }
        }
    }
}
