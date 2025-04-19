package com.example.campus_teamup.project.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.CheckEmptyFields
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.CreatePostViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostProject(createPostViewModel: CreatePostViewModel) {

    val context = LocalContext.current

    val teamName = remember {
        mutableStateOf("")
    }
    val hackathonOrPersonal = remember {
        mutableStateOf("")
    }

    val problemStatement = remember {
        mutableStateOf("")
    }
    val githubUrl = remember {
        mutableStateOf("")
    }
    
    val isLoading = createPostViewModel.isLoading.collectAsState()
    val isProjectPosted = createPostViewModel.isPosted.collectAsState()

    if(isProjectPosted.value){
        teamName.value = ""
        hackathonOrPersonal.value = ""
        problemStatement.value = ""
        githubUrl.value = ""
        ToastHelper.showToast(context , "Project Posted Successfully")
    }

    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(value = teamName.value,
            onValueChange = { teamName.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
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

        OutlinedTextField(value = hackathonOrPersonal.value,
            onValueChange = { hackathonOrPersonal.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            label = {
                Text(text = stringResource(id = R.string.hackathon_personal_project))
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.projects), contentDescription = null,
                    modifier = Modifier.size(22.dp), tint = White
                )
            }
        )

        OutlinedTextField(value = problemStatement.value,
            onValueChange = { problemStatement.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            label = {
                Text(text = stringResource(id = R.string.problem_statement))
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.problem_statement), contentDescription = null,
                    modifier = Modifier.size(22.dp), tint = White
                )
            }
        )

        OutlinedTextField(value = githubUrl.value,
            onValueChange = { githubUrl.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            label = {
                Text(text = stringResource(id = R.string.project_github_url))
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.github), contentDescription = null,
                )
            }
        )
        

        Row(horizontalArrangement = Arrangement.Center , verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 20.dp)) {
            if(isLoading.value){
                ProgressIndicator.showProgressBar(modifier = Modifier, canShow = isLoading.value)
            }
            else{

                OutlinedButton(
                    onClick = {

                        val allRequiredField =
                            CheckEmptyFields.checkProjectFields(
                                teamName.value,
                                hackathonOrPersonal.value,
                                problemStatement.value
                            )

                        if (allRequiredField) {

                            createPostViewModel.postProject(
                                LocalDate.now().toString(),
                                teamName.value,
                                hackathonOrPersonal.value,
                                problemStatement.value,
                                githubUrl.value,
                                0
                            )
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
}