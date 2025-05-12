package com.example.new_campus_teamup.myotp

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
import androidx.navigation.NavController
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.CheckEmptyFields
import com.example.new_campus_teamup.helper.ProgressIndicator
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.helper.rememberNetworkStatus
import com.example.new_campus_teamup.myAnimation.TextAnimation
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.White

@Composable
fun UserSignUpScreen(
    signUpLoginViewModel: SignUpLoginViewModel,
    navController: NavController,
    context : Activity
) {
    var email = remember { mutableStateOf("") }
    var name = remember { mutableStateOf("") }
    var phoneNumber = remember { mutableStateOf("") }
    val context = LocalContext.current
    val collegeName = remember { mutableStateOf("") }
    val textColor = White
    val isConnected = rememberNetworkStatus()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(isConnected) {
        if (!isConnected) {
            snackbarHostState.showSnackbar(
                message = "No Internet Connection",
                actionLabel = "OK"
            )
        }
    }

    val isVerificationInProgress = signUpLoginViewModel.isVerificationInProgress.collectAsState()

    val isPhoneVerificationSuccess = signUpLoginViewModel.isPhoneVerificationSuccess.collectAsState()

    // if verification completes navigate user to enter otp
    LaunchedEffect(isPhoneVerificationSuccess.value){
        if(isPhoneVerificationSuccess.value){
            navController.navigate("otpverification/${"signup"}/${phoneNumber.value}")
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor), contentAlignment = Alignment.Center
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (appLogo, welcomeMessage, progressBar, signUpHeading, emailField, collegeField, nameField,phoneNumberField,  signUp, login) = createRefs()


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


            PhoneNumberInputField(modifier = Modifier
                .constrainAs(phoneNumberField) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(collegeField.bottom, margin = 8.dp)
                }
                .fillMaxWidth(0.85f), phoneNumber)

            // signUpButton

            if (isVerificationInProgress.value) {
                ProgressIndicator.showProgressBar(
                    modifier = Modifier.constrainAs(progressBar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(phoneNumberField.bottom, margin = 20.dp)
                    },
                    isVerificationInProgress.value
                )
            } else {
            SignUpButton(modifier = Modifier.constrainAs(signUp) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(phoneNumberField.bottom, margin = 20.dp)
            } ,CheckEmptyFields.isSignUpDetailsAreCorrect(email.value , collegeName.value , name.value,phoneNumber.value )){


                if(!isConnected){
                    ToastHelper.showToast(context , "Network Connection Error")
                    return@SignUpButton
                }
                // first check if email is already registered or not

                signUpLoginViewModel.isEmailOrPhoneNumberRegistered(email.value ,phoneNumber.value ,  isEmailOrPhoneNumberRegistered = {
                    // if email or phone number is registered toast user to login with registered number
                    if(it){
                        ToastHelper.showToast(context ,"Email or Number is already registered \n Please use another email or Number ." )
                    }
                    else {
                        // Now sending otp to user and saving user data temporary in datastore
                        signUpLoginViewModel.startVerification(phoneNumber.value , context as Activity)
                        signUpLoginViewModel.saveUserData(email.value, name.value , collegeName.value , phoneNumber.value)
                    }
                })
            }
        }


            // login if already have account
            LoginButtonIfAlreadyAccount(modifier = Modifier.constrainAs(login) {
                start.linkTo(signUp.end)
                end.linkTo(parent.end)
                top.linkTo(if(isVerificationInProgress.value) progressBar.bottom else signUp.bottom, margin = 30.dp)
            } , navController)


        }
    }

}

@Composable
fun LoginButtonIfAlreadyAccount(modifier: Modifier, navController: NavController) {
    Text(text = stringResource(id = R.string.login),
        color = White, modifier = modifier.clickable {
            navController.navigate("login")
        })
}

@Composable
fun SignUpButton(modifier: Modifier, isAllDetailsCorrect: Boolean, onSignUpBtnClicked: () -> Unit) {
    OutlinedButton(
        onClick = {
                  onSignUpBtnClicked()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = BackGroundColor,
            contentColor = White
        ),
        enabled = isAllDetailsCorrect
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
fun PhoneNumberInputField(modifier: Modifier, phoneNumber: MutableState<String>) {


    OutlinedTextField(
        value = phoneNumber.value, onValueChange = { phoneNumber.value = it },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        maxLines = 3,
        label = {
            Text(text = stringResource(id = R.string.enter_phone_number))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.phone),
                contentDescription = stringResource(R.string.college_icon),
                modifier = Modifier.size(22.dp),
                tint = White
            )
        },
        modifier = modifier
    )
}
