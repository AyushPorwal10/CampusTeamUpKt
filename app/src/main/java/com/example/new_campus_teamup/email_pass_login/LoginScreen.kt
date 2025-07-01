package com.example.new_campus_teamup.email_pass_login

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.CheckEmptyFields
import com.example.new_campus_teamup.helper.ProgressIndicator
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myAnimation.TextAnimation
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.myactivities.MainActivity
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.White

@Composable
fun LoginScreen(
    loginSignUpViewModel: LoginSignUpViewModel,
    context: Activity,
    navController: NavHostController,
) {


    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val showPassword = remember { mutableStateOf(false) }


    val snackbarHostState = remember { SnackbarHostState() }

    val isLoading = loginSignUpViewModel.isLoading.collectAsState()

    val context = LocalContext.current
    val textColor = White


    LaunchedEffect(Unit){
        loginSignUpViewModel.errorMessage.collect{error->
            error?.let {
                ToastHelper.showToast(context ,error)
                loginSignUpViewModel.resetErrorMessage()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor), contentAlignment = Alignment.Center
    ) {

        ConstraintLayout() {
            val (appLogo, welComeText, loginHeading, emailField, passwordField ,forgotPassword,  progressBar, loginButton, signUp) = createRefs()


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

            //  Email input field

            UserEmailField(modifier = Modifier
                .constrainAs(emailField) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(loginHeading.bottom, margin = 20.dp)
                }
                .fillMaxWidth(0.85f), email)


            UserPasswordField(modifier = Modifier
                .constrainAs(passwordField) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(emailField.bottom, margin = 20.dp)
                }
                .fillMaxWidth(0.85f), password , showPassword)


            if(isLoading.value){
                ProgressIndicator.showProgressBar(
                    modifier = Modifier.constrainAs(progressBar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(passwordField.bottom, margin = 20.dp)
                    },
                    isLoading.value
                )
            }
            else {
                OutlinedButton(onClick = {
                    if(!CheckEmptyFields.isValidEmail(email.value.trim())){
                        ToastHelper.showToast(context , "Please Enter valid email")
                    }
                    else if(password.value.trim().isEmpty()){
                        ToastHelper.showToast(context , "Please Enter valid password")
                    }
                    else {
                        loginSignUpViewModel.signInUser(email.value , password.value , onSuccess = {

                            loginSignUpViewModel.fetchDataFromDatabase(loginSignUpViewModel.getUserId(email.value.trim()), onUserDataSaved = {
                                val intent = Intent(context , MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(intent)
                            })
                        })
                    }
                }, modifier = Modifier.constrainAs(loginButton){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(passwordField.bottom, margin = 12.dp)
                }) {
                    Text(text = stringResource(id = R.string.login), color = textColor)
                }
            }

            // new account sign up button
            NewAccountSignUpButton(modifier = Modifier.constrainAs(signUp) {
                end.linkTo(parent.end)
                top.linkTo(if(isLoading.value) progressBar.bottom else loginButton.bottom , margin = 20.dp)
            }, onClick = {
                navController.navigate("signup"){
                    popUpTo("login") {inclusive = false}
                    launchSingleTop = true
                }
            })

            // forgot password button
            ForgotPasswordButton(modifier = Modifier.constrainAs(forgotPassword) {
                start.linkTo(parent.start)
                top.linkTo(if(isLoading.value) progressBar.bottom else loginButton.bottom , margin = 20.dp)
            }, onClick = {
                navController.navigate("forgotpassword"){
                    popUpTo("login") {inclusive = false}
                    launchSingleTop = true
                }
            })
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
fun ForgotPasswordButton(modifier: Modifier, onClick: () -> Unit) {
    Text(text = stringResource(id = R.string.forgotPassword),
        color = White, modifier = modifier.clickable {
            onClick()
        })
}


@Composable
fun UserEmailField(modifier: Modifier, email: MutableState<String>) {
    OutlinedTextField(
        value = email.value,
        onValueChange = { email.value = it },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 1,
        label = {
            Text(text = stringResource(id = R.string.enter_email))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.email), contentDescription = "Phone Icon",
                modifier = Modifier.size(22.dp), tint = White
            )
        }, modifier = modifier
    )
}

@Composable
fun UserPasswordField(
    modifier: Modifier,
    password: MutableState<String>,
    showPassword: MutableState<Boolean>
) {
    OutlinedTextField(
        value = password.value,
        onValueChange = { password.value = it },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 1,
        label = {
            Text(text = stringResource(id = R.string.enter_password))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.password), contentDescription = "Password Icon",
                modifier = Modifier.size(22.dp), tint = White
            )
        },
        trailingIcon = {
            Icon(
                painterResource( if(showPassword.value)R.drawable.showpass else R.drawable.hidepass), contentDescription = "Eye Icon",
                modifier = Modifier.size(22.dp).clickable {
                    showPassword.value = !showPassword.value
                }, tint = White
            )
        },
        visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier
    )
}

