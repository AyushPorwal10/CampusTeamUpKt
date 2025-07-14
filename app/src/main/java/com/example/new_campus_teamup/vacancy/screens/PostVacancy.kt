package com.example.new_campus_teamup.vacancy.screens

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.CheckEmptyFields
import com.example.new_campus_teamup.helper.ProgressIndicator
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myAnimation.FloatingBubbles
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.ButtonColor
import com.example.new_campus_teamup.ui.theme.IconColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.CreatePostViewModel
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostVacancy(
    createPostViewModel : CreatePostViewModel
) {

    val isLoading = createPostViewModel.isLoading.collectAsState()

    val scrollState = rememberScrollState()
    val teamName = remember { mutableStateOf("") }
    val roleLookingFor = remember { mutableStateOf("") }
    val hackathonName = remember { mutableStateOf("") }
    val skills = remember { mutableStateOf("") }
    val roleDescription = remember { mutableStateOf("") }

    val context = LocalContext.current

    var selectedTeamLogo by remember { mutableStateOf<String>("") }


    LaunchedEffect(Unit){
        createPostViewModel.errorMessage.collect{error->
            error?.let {
                ToastHelper.showToast(context ,error)
                createPostViewModel.clearError()
            }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            Log.d("Vacancy","Logo selected $uri")
            selectedTeamLogo = uri.toString()
        }
        else{
            ToastHelper.showToast(context , "No Image Selected")
        }
    }




    Box(modifier = Modifier.fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = BackgroundGradientColor,
                startY = 0f,
                endY = Float.POSITIVE_INFINITY
            )
        ),
    ){

        FloatingBubbles()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Card(modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Post Vacancy",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                    Spacer(modifier = Modifier.height(32.dp))


                    // Team Logo
                    IconButton(onClick = {
                        imagePickerLauncher.launch("image/*")
                    },modifier = Modifier.size(80.dp)) {
                        AsyncImage(
                            model = selectedTeamLogo.ifBlank { R.drawable.vacancies },
                            contentDescription = "User Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(1.dp, White, CircleShape))
                    }

                    Spacer(modifier = Modifier.height(10.dp))


                    // Team Name

                    OutlinedTextField(value = teamName.value,
                        onValueChange = { teamName.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldStyle.myTextFieldColor(),
                        shape = TextFieldStyle.defaultShape,
                        maxLines = 2,
                        label = {
                            Text(text = stringResource(id = R.string.enter_team_name))
                        },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.vacancies), contentDescription = null,
                                modifier = Modifier.size(22.dp), tint = IconColor
                            )
                        }
                    )

                    // Hackathon Name

                    OutlinedTextField(value = hackathonName.value,
                        onValueChange = { hackathonName.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldStyle.myTextFieldColor(),
                        shape = TextFieldStyle.defaultShape,
                        maxLines = 2,
                        label = {
                            Text(text = stringResource(id = R.string.hackathon_name))
                        },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.hackathon), contentDescription = null,
                                modifier = Modifier.size(22.dp), tint = IconColor
                            )
                        }
                    )


                    // Role Looking For
                    OutlinedTextField(value = roleLookingFor.value,
                        onValueChange = { roleLookingFor.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldStyle.myTextFieldColor(),
                        shape = TextFieldStyle.defaultShape,
                        maxLines = 2,
                        label = {
                            Text(text = stringResource(id = R.string.role_looking_for))
                        },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.profile), contentDescription = null,
                                modifier = Modifier.size(22.dp), tint = IconColor
                            )
                        }
                    )


                    // Skills Required

                    OutlinedTextField(value = skills.value,
                        onValueChange = { skills.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldStyle.myTextFieldColor(),
                        shape = TextFieldStyle.defaultShape,
                        maxLines = 2,
                        label = {
                            Text(text = stringResource(id = R.string.skills_needed))
                        },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.skills), contentDescription = null,
                                modifier = Modifier.size(22.dp), tint = IconColor
                            )
                        }
                    )


                    OutlinedTextField(value = roleDescription.value,
                        onValueChange = { roleDescription.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldStyle.myTextFieldColor(),
                        shape = TextFieldStyle.defaultShape,
                        label = {
                            Text(text = stringResource(id = R.string.describe_role))
                        },
                    )


                    Spacer(modifier = Modifier.height(16.dp))




                    Button(
                        onClick = {
                            val isAllRequiredFieldsCorrect = CheckEmptyFields.checkVacancyFields(
                                teamName.value,
                                hackathonName.value,
                                roleLookingFor.value,
                                skills.value,
                            )

                            if(isAllRequiredFieldsCorrect){

                                createPostViewModel.uploadTeamLogo(selectedTeamLogo) {canPost ,  url ->

                                    if(canPost){
                                        val downloadImageUrl = url ?: ""
                                        createPostViewModel.postVacancy(
                                            LocalDate.now().toString(),
                                            downloadImageUrl,
                                            teamName.value,
                                            hackathonName.value,
                                            roleLookingFor.value,
                                            skills.value,
                                            roleDescription.value , onVacancyPosted = {
                                                teamName.value = ""
                                                hackathonName.value  =""
                                                roleDescription.value = ""
                                                skills.value = ""
                                                roleLookingFor.value = ""
                                                roleDescription.value = ""
                                                ToastHelper.showToast(context , "Vacancy Posted Successfully")
                                            })
                                    }
                                    else
                                        ToastHelper.showToast(context , "You can post only 4 vacancy")

                                }
                            }
                            else{
                                ToastHelper.showToast(context , "All * marked fields are required")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = ButtonColor
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading.value) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "Post",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                }
            }
        }
    }
}