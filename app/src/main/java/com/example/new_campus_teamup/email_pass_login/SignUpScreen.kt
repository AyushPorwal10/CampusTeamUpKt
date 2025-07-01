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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
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
fun SignUpScreen(
    loginSignUpViewModel: LoginSignUpViewModel,
    navController: NavController,
    context: Activity
) {
    var email = remember { mutableStateOf("") }
    var name = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    val showPassword = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val collegeName = remember { mutableStateOf("") }
    val textColor = White
    val snackbarHostState = remember { SnackbarHostState() }

    val isLoading = loginSignUpViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        loginSignUpViewModel.errorMessage.collect { error ->
            error?.let {
                ToastHelper.showToast(context, error)
                loginSignUpViewModel.resetErrorMessage()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor),
        contentAlignment = Alignment.Center
    ) {
        ConstraintLayout() {
            val (appLogo, welcomeMessage, progressBar, signUpHeading, emailField, collegeField, nameField, passwordField, signUp, login) = createRefs()
            Image(
                painterResource(id = R.drawable.app_logo),
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


            NameInputField(
                modifier = Modifier
                    .constrainAs(nameField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(signUpHeading.bottom, margin = 20.dp)
                    }
                    .fillMaxWidth(0.85f), name)


            // college name
            CollegeNameField(
                modifier = Modifier
                    .constrainAs(collegeField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(nameField.bottom, margin = 8.dp)
                    }
                    .fillMaxWidth(0.85f), collegeName)




            EmailInputField(
                modifier = Modifier
                    .constrainAs(emailField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(collegeField.bottom, margin = 8.dp)
                    }
                    .fillMaxWidth(0.85f), email)

            PasswordField(
                modifier = Modifier
                    .constrainAs(passwordField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(emailField.bottom, margin = 8.dp)
                    }
                    .fillMaxWidth(0.85f), password, showPassword)

            // signUpButton

            if (isLoading.value) {
                ProgressIndicator.showProgressBar(
                    modifier = Modifier.constrainAs(progressBar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(passwordField.bottom, margin = 20.dp)
                    },
                    isLoading.value
                )
            } else {
                SignUpButton(modifier = Modifier.constrainAs(signUp) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(passwordField.bottom, margin = 20.dp)
                }) {
                    if (name.value.isEmpty()) {
                        ToastHelper.showToast(context, "Please Enter Name")
                    } else if (collegeName.value.isEmpty()) {
                        ToastHelper.showToast(context, "Please Enter College Name")
                    } else if (!CheckEmptyFields.isValidEmail(email.value)) {
                        ToastHelper.showToast(context, "Please Enter Valid Email")
                    } else if (password.value.isEmpty() || password.value.length < 6) {
                        ToastHelper.showToast(context, "Please enter 6 digit password")
                    } else {
                        loginSignUpViewModel.signUpUser(email.value, password.value, onSuccess = {
                            loginSignUpViewModel.saveUserData(
                                email.value,
                                name.value,
                                collegeName.value,
                                "Signup",
                                onUserDataSaved = {
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(intent)
                                })
                        })
                    }
                }
            }


            // login if already have account
            LoginButtonIfAlreadyAccount(modifier = Modifier.constrainAs(login) {
                start.linkTo(if (isLoading.value) progressBar.end else signUp.end)
                end.linkTo(parent.end)
                top.linkTo(
                    if (isLoading.value) progressBar.bottom else signUp.bottom,
                    margin = 30.dp
                )
            }, navController)


        }
    }

}

@Composable
fun LoginButtonIfAlreadyAccount(modifier: Modifier, navController: NavController) {
    Text(
        text = stringResource(id = R.string.login),
        color = White, modifier = modifier.clickable {
            navController.navigate("login"){
                popUpTo("signup") {inclusive = false} // this will keep signup
                launchSingleTop = true
            }
        })
}

@Composable
fun SignUpButton(modifier: Modifier, onSignUpBtnClicked: () -> Unit) {
    OutlinedButton(
        onClick = {
            onSignUpBtnClicked()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = BackGroundColor,
            contentColor = White
        ),
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
fun NameInputField(
    modifier: Modifier,
    name: MutableState<String>,
) {


    OutlinedTextField(
        value = name.value, onValueChange = {
            name.value = it
        },
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
        maxLines = 3,
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


@Composable
fun PasswordField(
    modifier: Modifier,
    password: MutableState<String>,
    showPassword: MutableState<Boolean>
) {


    OutlinedTextField(
        value = password.value, onValueChange = { password.value = it },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 3,
        label = {
            Text(text = stringResource(id = R.string.enter_password))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.password),
                contentDescription = stringResource(R.string.college_icon),
                modifier = Modifier.size(22.dp),
                tint = White
            )
        },
        trailingIcon = {
            Icon(
                painterResource(if (showPassword.value) R.drawable.showpass else R.drawable.hidepass),
                contentDescription = "Eye Icon",
                modifier = Modifier
                    .size(22.dp)
                    .clickable {
                        showPassword.value = !showPassword.value
                    },
                tint = White
            )
        },
        visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier
    )
}
