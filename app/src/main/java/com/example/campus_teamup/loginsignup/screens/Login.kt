package com.example.campus_teamup.loginsignup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campus_teamup.helper.AskForEmailVerification
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.CheckEmptyFields
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.myAnimation.TextAnimation
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.LoginViewModel
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    navigateToSignUpScreen: () -> Unit = {},
    navigateToHomeScreen: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel() // here you should ask why write hiltViewModel here jab ki hum activity se pass kar rahe he
) {

    var email = remember { mutableStateOf("") }
    val context = LocalContext.current
    val textColor = White
    val isEmailSent by loginViewModel._isEmailSent.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val showProgressBar = remember { mutableStateOf(false) }

    if (isEmailSent) {
        showProgressBar.value = false
        LaunchedEffect(Unit){
            loginViewModel.saveUserData(email.value)
        }
        AskForEmailVerification()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor), contentAlignment = Alignment.Center
    ) {

        ConstraintLayout() {
            val (appLogo, welComeText, loginHeading, emailField, progressBar, loginButton, forgotPassword, signUp) = createRefs()

            Image(
                painterResource(id = R.drawable.app_logo), contentDescription = stringResource(
                    id = R.string.app_name
                ), modifier = Modifier
                    .constrainAs(appLogo) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 20.dp)
                    }
                    .size(100.dp))



            TextAnimation.AnimatedText(modifier = Modifier.constrainAs(welComeText) {
                top.linkTo(appLogo.bottom, margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            Text(
                text = stringResource(id = R.string.login_here),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.constrainAs(loginHeading) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(welComeText.bottom, margin = 40.dp)
                },
                color = textColor,
                fontWeight = FontWeight.Bold

            )

            // email

            LoginEmailInputField(modifier = Modifier
                .constrainAs(emailField) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(loginHeading.bottom, margin = 20.dp)
                }
                .fillMaxWidth(0.85f), email)



            if(showProgressBar.value){
                ProgressIndicator.showProgressBar(modifier = Modifier
                    .constrainAs(progressBar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(emailField.bottom, margin = 20.dp)
                    }, showProgressBar.value)
            }
            else {
                LoginButton(modifier = Modifier
                    .constrainAs(loginButton) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(emailField.bottom, margin = 20.dp)
                    },
                    signInWithEmail = {
                        val checkEmail = CheckEmptyFields.checkEmail(email.value.trim())
                        if (checkEmail == "") {
                            showProgressBar.value = true
                            coroutineScope.launch {
                                loginViewModel.signInWithEmailLink(email.value.trim(),
                                    onFailure = {
                                        showProgressBar.value = false
                                        ToastHelper.showToast(context, it.message + "")
                                    })
                            }

                        } else
                            ToastHelper.showMessageForEmptyEmail(context, email.value.trim())

                    })
            }


            // new account sign up button
            NewAccountSignUpButton(modifier = Modifier.constrainAs(signUp) {
                end.linkTo(parent.end)
                top.linkTo(if(showProgressBar.value) progressBar.bottom else loginButton.bottom, margin = 30.dp)
            }, onClick = navigateToSignUpScreen)
        }
    }
}

@Composable
fun NewAccountSignUpButton(modifier: Modifier, onClick: () -> Unit) {
    Text(text = stringResource(id = R.string.signUp),
        color = White, modifier = modifier.clickable {
            onClick()
        })
}


@Composable
fun LoginButton(modifier: Modifier, signInWithEmail: () -> Unit) {

    OutlinedButton(onClick = {
        signInWithEmail()
    },
        colors = ButtonDefaults.buttonColors(
            containerColor = BackGroundColor,
            contentColor = White
        ), modifier = modifier)
    {
        Text(
            text = stringResource(id = R.string.Login),
            color = White
        )
    }

}

@Composable
fun LoginEmailInputField(modifier: Modifier, email: MutableState<String>) {

    OutlinedTextField(value = email.value,
        onValueChange = { email.value = it },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 1,
        label = {
            Text(text = stringResource(id = R.string.enter_email))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.email), contentDescription = "Email Icon",
                modifier = Modifier.size(22.dp), tint = White
            )
        }, modifier = modifier)
}
