package com.example.campus_teamup.myotp

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.helper.rememberNetworkStatus
import com.example.campus_teamup.myAnimation.TextAnimation
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White


@Composable
fun LoginWithOtp(
    signUpLoginViewModel: SignUpLoginViewModel,
    context: Activity,
    navController: NavHostController,
) {

    var phoneNumber = remember { mutableStateOf("") }

    val isConnected = rememberNetworkStatus()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val textColor = White
    val isPhoneVerificationSuccess =
        signUpLoginViewModel.isPhoneVerificationSuccess.collectAsState()
    val isPhoneVerificationInProgress =
        signUpLoginViewModel.isVerificationInProgress.collectAsState()


    LaunchedEffect(isPhoneVerificationSuccess.value) {
        if (isPhoneVerificationSuccess.value) {
            navController.navigate("otpverification/${"login"}/${phoneNumber.value}")
        }
    }


    if(!isConnected){
        ToastHelper.showToast(context , "No network connection.")
    }




    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor), contentAlignment = Alignment.Center
    ) {

        ConstraintLayout() {
            val (appLogo, welComeText, loginHeading, phoneNumberField, progressBar, loginButton, signUp) = createRefs()

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

            // mobile number

            LoginPhoneNumberInputField(modifier = Modifier
                .constrainAs(phoneNumberField) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(loginHeading.bottom, margin = 20.dp)
                }
                .fillMaxWidth(0.85f), phoneNumber)




            if (isPhoneVerificationInProgress.value) {
                ProgressIndicator.showProgressBar(modifier = Modifier
                    .constrainAs(loginButton) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(phoneNumberField.bottom, margin = 20.dp)
                    }, canShow = isPhoneVerificationInProgress.value
                )
            }
            else if(phoneNumber.value.length == 10 ) {
                SendOtp(modifier = Modifier
                    .constrainAs(loginButton) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(phoneNumberField.bottom, margin = 20.dp)
                    },
                    signInWithPhoneNumber = {
                        if (phoneNumber.value.isNotBlank()) {
                            signUpLoginViewModel.isEmailOrPhoneNumberRegistered(
                                "test@gmail", // no need to check for email when user login
                                phoneNumber.value,

                                // if phone number is already there you can start sending otp
                                isEmailOrPhoneNumberRegistered = {
                                    if (it) {
                                        signUpLoginViewModel.startVerification(
                                            phoneNumber.value,
                                            context as Activity
                                        )
                                    } else {
                                        ToastHelper.showToast(
                                            context,
                                            "No account registered \n Please SignUp ."
                                        )
                                    }
                                })

                        }
                    } , isConnected)

            }



            // new account sign up button
            NewAccountSignUpButton(modifier = Modifier.constrainAs(signUp) {
                end.linkTo(parent.end)
                top.linkTo(phoneNumberField.bottom , margin = 20.dp)
            }, onClick = {
                navController.navigate("signup")
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
fun SendOtp(modifier: Modifier, signInWithPhoneNumber: () -> Unit ,isConnect : Boolean) {

    OutlinedButton(
        onClick = {
            signInWithPhoneNumber()
        },
        enabled = isConnect,
        colors = ButtonDefaults.buttonColors(
            containerColor = BackGroundColor,
            contentColor = White
        ), modifier = modifier
    )
    {
        Text(
            text = stringResource(id = R.string.Login),
            color = White
        )
    }

}

@Composable
fun LoginPhoneNumberInputField(modifier: Modifier, phoneNumber: MutableState<String>) {

    OutlinedTextField(
        value = phoneNumber.value,
        onValueChange = { phoneNumber.value = it },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text(text = stringResource(id = R.string.enter_phone_number))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.phone), contentDescription = "Phone Icon",
                modifier = Modifier.size(22.dp), tint = White
            )
        }, modifier = modifier
    )
}
