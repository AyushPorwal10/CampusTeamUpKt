package com.example.campus_teamup.screens

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_teamup.AskForEmailVerification
import com.example.campus_teamup.MainActivity
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.CheckEmptyFields
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.myAnimation.TextAnimation
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.SignUpViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = viewModel(),
    navigateToLoginScreen: () -> Unit
) {
    val context = LocalContext.current
    var email = remember { mutableStateOf("") }
    var name = remember { mutableStateOf("") }
    val loading = remember { mutableStateOf(false) }

    val collegeName = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val textColor = White

    val emailSentState by signUpViewModel.isEmailSent.collectAsState()

    var showEmailVerificationDialog = remember { mutableStateOf(false) }



    if (emailSentState) {

            LaunchedEffect(Unit){
                signUpViewModel.saveUserData(email.value , name.value , collegeName.value)
            }

        showEmailVerificationDialog.value = true
        AskForEmailVerification {
            showEmailVerificationDialog.value = false
        }
    }
    if(loading.value){
        ProgressIndicator.showProgressBar(loading)
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor), contentAlignment = Alignment.Center
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (appLogo, welcomeMessage, signUpHeading, emailField, passwordField, collegeField, nameField, signUp, login) = createRefs()


            Image(painterResource(id = R.drawable.app_logo),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .constrainAs(appLogo) {
                        top.linkTo(parent.top, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .size(100.dp))


            TextAnimation.AnimatedText(modifier = Modifier.constrainAs(welcomeMessage) {
                top.linkTo(appLogo.bottom, margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })


            Text(
                text = stringResource(id = R.string.signup_here),
                modifier = Modifier.constrainAs(signUpHeading) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(welcomeMessage.bottom, margin = 40.dp)
                },
                color = textColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )


            // email

            // name

            NameInputField(modifier = Modifier
                .constrainAs(nameField) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(signUpHeading.bottom, margin = 20.dp)
                }
                .fillMaxWidth(0.85f), name)


            EmailInputField(modifier = Modifier
                .constrainAs(emailField) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(nameField.bottom, margin = 8.dp)
                }
                .fillMaxWidth(0.85f), email)


            // college name
            CollegeNameField(modifier = Modifier
                .constrainAs(collegeField) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(emailField.bottom, margin = 8.dp)
                }
                .fillMaxWidth(0.85f), collegeName)


            // signUpButton

            SignUpButton(modifier = Modifier.constrainAs(signUp) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(collegeField.bottom, margin = 20.dp)
            }) {
                // checking if fields are not empty
                val checkEmail = CheckEmptyFields.checkEmail(email.value.trim())
                val checkName = CheckEmptyFields.checkName(name.value.trim())


                if (checkEmail == "" && checkName == "")
                    coroutineScope.launch {
                        loading.value = true
                        signUpViewModel.signUpWithEmailLink(
                            email.value.trim(),
                            onSuccess = {
                                loading.value = false
                                Log.d("Signup", "Email Sent Success")
                            },
                            onFailure = {
                                loading.value = false
                                if (it.message == "Already registered")
                                    ToastHelper.showToast(
                                        context,
                                        "Email is already registered. Please Log-in"
                                    )
                                else
                                    ToastHelper.showToast(context, "Something went wrong !")
                            })
                    }
                else if (checkName == "") ToastHelper.showMessageForEmptyName(
                    context,
                    name.value.trim()
                )
                else if (checkEmail == "") ToastHelper.showMessageForEmptyEmail(
                    context,
                    email.value.trim()
                )
            }


            // login if already have account
            LoginButtonIfAlreadyAccount(modifier = Modifier.constrainAs(login) {
                start.linkTo(signUp.end)
                end.linkTo(parent.end)
                top.linkTo(signUp.bottom, margin = 30.dp)
            }, navigateToLoginScreen)
        }
    }

}

@Composable
fun LoginButtonIfAlreadyAccount(modifier: Modifier, onClick: () -> Unit) {
    Text(text = stringResource(id = R.string.login),
        color = White, modifier = modifier.clickable {
            onClick()
        })
}

@Composable
fun SignUpButton(modifier: Modifier, signUpWithEmail: () -> Unit) {

    OutlinedButton(
        onClick = { signUpWithEmail() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = BackGroundColor,
            contentColor = White
        )
    ) {
        Text(text = stringResource(id = R.string.signUp))

    }
}

@Composable
fun EmailInputField(modifier: Modifier, email: MutableState<String>) {


    OutlinedTextField(
        value = email.value, onValueChange = {
            email.value = it

        },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 1,
        label = {
            Text(text = stringResource(id = R.string.enter_email))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.email),
                contentDescription = stringResource(R.string.email),
                modifier = Modifier.size(22.dp),
                tint = White
            )
        },
        modifier = modifier
    )
}

@Composable
fun NameInputField(modifier: Modifier, name: MutableState<String>) {


    OutlinedTextField(
        value = name.value, onValueChange = { name.value = it },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 1,
        label = {
            Text(text = stringResource(id = R.string.enter_name))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.profile),
                contentDescription = stringResource(R.string.user_profile_icon),
                modifier = Modifier.size(22.dp),
                tint = White
            )
        },
        modifier = modifier
    )
}



@Composable
fun CollegeNameField(modifier: Modifier, collegeName: MutableState<String>) {


    OutlinedTextField(
        value = collegeName.value, onValueChange = { collegeName.value = it },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 1,
        label = {
            Text(text = stringResource(id = R.string.enter_college_name))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.college),
                contentDescription = stringResource(R.string.college_icon),
                modifier = Modifier.size(22.dp),
                tint = White
            )
        },
        modifier = modifier
    )
}
