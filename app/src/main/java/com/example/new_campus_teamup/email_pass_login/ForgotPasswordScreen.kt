package com.example.new_campus_teamup.email_pass_login
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.new_campus_teamup.helper.EmailSentDialog
import com.example.new_campus_teamup.helper.ProgressIndicator
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myAnimation.TextAnimation
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.myactivities.MainActivity
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    loginSignUpViewModel: LoginSignUpViewModel,
    navController: NavHostController,
) {
    val email = remember { mutableStateOf("") }
    var emailSentDialog by remember {mutableStateOf(false)}



    val isLoading = loginSignUpViewModel.isLoading.collectAsState()

    val context = LocalContext.current
    val textColor = White

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(emailSentDialog) {
        if (emailSentDialog) {
            scope.launch {
                snackbarHostState.showSnackbar(message = "Email sent successfully" ,
                    actionLabel = "OK")
            }
            emailSentDialog = false
        }
    }
    LaunchedEffect(Unit){
        loginSignUpViewModel.errorMessage.collect{error->
            error?.let {
                ToastHelper.showToast(context ,error)
                loginSignUpViewModel.resetErrorMessage()
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {paddingValues->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

                ChangePasswordEmailInput(modifier = Modifier
                    .constrainAs(emailField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(loginHeading.bottom, margin = 20.dp)
                    }
                    .fillMaxWidth(0.85f), email)



                if(isLoading.value){
                    ProgressIndicator.showProgressBar(
                        modifier = Modifier.constrainAs(progressBar) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(emailField.bottom, margin = 20.dp)
                        },
                        isLoading.value
                    )
                }
                else {
                    OutlinedButton(onClick = {
                        if(!CheckEmptyFields.isValidEmail(email.value.trim())){
                            ToastHelper.showToast(context , "Please Enter valid email")
                        }
                        else {
                            loginSignUpViewModel.isEmailRegistered(email.value , isRegistered = {
                                if(it){
                                    loginSignUpViewModel.sendPasswordResetEmail(email.value , isEmailSent = {
                                        emailSentDialog = true
                                    })
                                }
                            })
                        }
                    }, modifier = Modifier.constrainAs(loginButton){
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(emailField.bottom, margin = 12.dp)
                    }) {
                        Text(text = stringResource(id = R.string.change_password), color = textColor)
                    }
                }

                // login sign up button
                LoginButton(modifier = Modifier.constrainAs(signUp) {
                    end.linkTo(parent.end)
                    top.linkTo(if(isLoading.value) progressBar.bottom else loginButton.bottom , margin = 20.dp)
                }, onClick = {
                    navController.navigate("login"){
                        popUpTo("forgotpassword") {inclusive = false}
                        launchSingleTop = true
                    }
                })
            }
        }
    }

}

@Composable
fun LoginButton(modifier: Modifier, onClick: () -> Unit) {
    Text(text = stringResource(id = R.string.login),
        color = White, modifier = modifier.clickable {
            onClick()
        })
}

@Composable
fun ChangePasswordEmailInput(modifier: Modifier, email: MutableState<String>) {
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
fun ChangePasswordField(
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

