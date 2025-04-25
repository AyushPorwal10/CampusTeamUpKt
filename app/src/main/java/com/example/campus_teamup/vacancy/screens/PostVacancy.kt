package com.example.campus_teamup.vacancy.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.CheckEmptyFields
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.CreatePostViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun PostVacancy(createPostViewModel : CreatePostViewModel) {

    val isLoading = createPostViewModel.isLoading.collectAsState()

    val scrollState = rememberScrollState()
    val teamName = remember { mutableStateOf("") }
    val roleLookingFor = remember { mutableStateOf("") }
    val hackathonName = remember { mutableStateOf("") }
    val skills = remember { mutableStateOf("") }
    val roleDescription = remember { mutableStateOf("") }

    val context = LocalContext.current

    var selectedTeamLogo by remember { mutableStateOf<String>("") }

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
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        // Team Logo
        IconButton(onClick = {
                             imagePickerLauncher.launch("image/*")
        },modifier = Modifier.size(80.dp)) {
            AsyncImage(
                model = selectedTeamLogo ?: R.drawable.vacancies,
                contentDescription = "User Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(1.dp, White, CircleShape))
        }





        // Team Name

        OutlinedTextField(value = teamName.value,
            onValueChange = { teamName.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            maxLines = 2,
            label = {
                Text(text = stringResource(id = R.string.enter_team_name))
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.vacancies), contentDescription = null,
                    modifier = Modifier.size(22.dp), tint = White
                )
            }
        )

        // Hackathon Name

        OutlinedTextField(value = hackathonName.value,
            onValueChange = { hackathonName.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            maxLines = 2,
            label = {
                Text(text = stringResource(id = R.string.hackathon_name))
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.hackathon), contentDescription = null,
                    modifier = Modifier.size(22.dp), tint = White
                )
            }
        )


        // Role Looking For
        OutlinedTextField(value = roleLookingFor.value,
            onValueChange = { roleLookingFor.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            maxLines = 2,
            label = {
                Text(text = stringResource(id = R.string.role_looking_for))
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.profile), contentDescription = null,
                    modifier = Modifier.size(22.dp), tint = White
                )
            }
        )


        // Skills Required

        OutlinedTextField(value = skills.value,
            onValueChange = { skills.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            maxLines = 2,
            label = {
                Text(text = stringResource(id = R.string.skills_needed))
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.skills), contentDescription = null,
                    modifier = Modifier.size(22.dp), tint = White
                )
            }
        )


        OutlinedTextField(value = roleDescription.value,
            onValueChange = { roleDescription.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            label = {
                Text(text = stringResource(id = R.string.describe_role))
            },
        )


        if(isLoading.value){
            ProgressIndicator.showProgressBar(
                modifier = Modifier,
                true
            )
        }
        else{
            OutlinedButton(
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = BackGroundColor,
                    contentColor = White
                )
            )
            {
                Text(
                    text = stringResource(id = R.string.post_role),
                    color = White
                )
            }
        }



    }
}