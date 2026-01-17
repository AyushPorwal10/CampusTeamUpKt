package com.example.new_campus_teamup.project.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    LaunchedEffect(Unit) {
        createPostViewModel.postUiEvent.collect { message->
            teamName.value = ""
            hackathonOrPersonal.value = ""
            problemStatement.value = ""
            githubUrl.value = ""
            ToastHelper.showToast(context , message)
        }
    }
    LaunchedEffect(Unit){
        createPostViewModel.errorMessage.collect{error->
            error?.let {
                ToastHelper.showToast(context ,error)
                createPostViewModel.clearError()
            }
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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
                        text = "Post Project",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                    Spacer(modifier = Modifier.height(32.dp))


                    OutlinedTextField(value = teamName.value,
                        onValueChange = { teamName.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2,
                        colors = TextFieldStyle.myTextFieldColor(),
                        shape = TextFieldStyle.defaultShape,
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

                    OutlinedTextField(value = hackathonOrPersonal.value,
                        onValueChange = { hackathonOrPersonal.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2,
                        colors = TextFieldStyle.myTextFieldColor(),
                        shape = TextFieldStyle.defaultShape,
                        label = {
                            Text(text = stringResource(id = R.string.hackathon_personal_project))
                        },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.projects), contentDescription = null,
                                modifier = Modifier.size(22.dp), tint = IconColor
                            )
                        }
                    )

                    OutlinedTextField(value = problemStatement.value,
                        onValueChange = { problemStatement.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldStyle.myTextFieldColor(),
                        shape = TextFieldStyle.defaultShape,
                        label = {
                            Text(text = stringResource(id = R.string.problem_statement))
                        },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.problem_statement), contentDescription = null,
                                modifier = Modifier.size(22.dp), tint = IconColor
                            )
                        }
                    )

                    OutlinedTextField(value = githubUrl.value,
                        onValueChange = { githubUrl.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldStyle.myTextFieldColor(),
                        maxLines = 2,
                        shape = TextFieldStyle.defaultShape,
                        placeholder = {
                            Text(text = stringResource(id = R.string.project_github_url))
                        },
                        label = {

                            Text(text = stringResource(id = R.string.github_url))
                        },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.github), contentDescription = null, tint = IconColor
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = {
                            val allRequiredField =
                                CheckEmptyFields.checkProjectFields(
                                    teamName.value,
                                    hackathonOrPersonal.value,
                                    problemStatement.value
                                )


                            if (allRequiredField) {

                                if(CheckEmptyFields.isValidHttpsUrl(githubUrl.value)){
                                    createPostViewModel.postProject(
                                        LocalDate.now().toString(),
                                        teamName.value,
                                        hackathonOrPersonal.value,
                                        problemStatement.value,
                                        githubUrl.value,
                                        0
                                    )
                                }
                                else {
                                    ToastHelper.showToast(context , "Please enter valid github link.")
                                }

                            }
                            else{
                                ToastHelper.showToast(context , "All fields are required.")
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