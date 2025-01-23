package com.example.campus_teamup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White


@Composable
fun SignUpScreen(
    navigateToHomeScreen : () -> Unit = {}
) {




    val textColor = White
    Box(modifier = Modifier
        .fillMaxSize()
        .background(BackGroundColor) , contentAlignment = Alignment.Center){


        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (appLogo , welcomeMessage , loginHeading , emailField , passwordField , nameField , collegeField , signUp, login ) = createRefs()


            Image(painterResource(id = R.drawable.app_logo) ,
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .constrainAs(appLogo) {
                        top.linkTo(parent.top, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .size(100.dp))

            Text(
                text = stringResource(id = R.string.welcomeGreeting) ,
                color = textColor ,
                modifier = Modifier.constrainAs(welcomeMessage){
                    top.linkTo(appLogo.bottom , margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                } , style = MaterialTheme.typography.titleMedium)

            Text(
                text = stringResource(id = R.string.signup_here),
                modifier = Modifier.constrainAs(loginHeading){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(welcomeMessage.bottom , margin = 40.dp)
                },
                color = textColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )

            // email

            EmailInputField(modifier = Modifier.constrainAs(emailField){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(loginHeading.bottom , margin = 20.dp)
            })


            // pass

            PasswordInputField(modifier = Modifier.constrainAs(passwordField){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(emailField.bottom , margin = 8.dp)
            })


            // name
            
            NameInputField(modifier = Modifier.constrainAs(nameField){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(passwordField.bottom , margin = 8.dp)
            })


            // college
            CollegeNameField(modifier = Modifier.constrainAs(collegeField){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(nameField.bottom, margin = 8.dp)
            })

            // signUpButton

            SignUpButton(modifier = Modifier.constrainAs(signUp){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(collegeField.bottom , margin = 20.dp)
            }.clickable {
                navigateToHomeScreen()
            })


            // login if already have account
            LoginButtonIfAlreadyAccount(modifier = Modifier.constrainAs(login){
                start.linkTo(signUp.end)
                end.linkTo(parent.end)
                top.linkTo(signUp.bottom , margin = 30.dp)
            }.clickable {  })
        }
    }

}

@Composable
fun LoginButtonIfAlreadyAccount(modifier: Modifier) {
    Text(text = stringResource(id = R.string.login),
        color = White , modifier = modifier)
}

@Composable
fun SignUpButton(modifier: Modifier) {

    OutlinedButton(onClick = { /*TODO*/ },
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
fun EmailInputField(modifier: Modifier) {
    var email = remember {
        mutableStateOf("")
    }

    OutlinedTextField(value = email.value, onValueChange = {email.value = it},
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 1,
        label = {
            Text(text = stringResource(id = R.string.enter_email))
        },
        leadingIcon = {
            Icon(painterResource(id = R.drawable.email), contentDescription = stringResource(R.string.email),
                modifier = Modifier.size(22.dp),tint = White
            )
        },
        modifier = modifier)
}

@Composable
fun CollegeNameField(modifier: Modifier) {

    var collegeName = remember {
        mutableStateOf("")
    }

    OutlinedTextField(value = collegeName.value, onValueChange = {collegeName.value = it},
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 1,
        label = {
            Text(text = stringResource(id = R.string.enter_college_name))
        },
        leadingIcon = {
            Icon(painterResource(id = R.drawable.college), contentDescription = stringResource(R.string.college_icon),
                modifier = Modifier.size(22.dp),tint = White
            )
        },
        modifier = modifier)
}

@Composable
fun NameInputField(modifier: Modifier) {

    var name = remember {
        mutableStateOf("")
    }

    OutlinedTextField(value = name.value, onValueChange = {name.value = it},
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 1,
        label = {
            Text(text = stringResource(id = R.string.enter_name))
        },
        leadingIcon = {
            Icon(painterResource(id = R.drawable.profile), contentDescription = stringResource(R.string.user_profile_icon),
                modifier = Modifier.size(22.dp),tint = White
            )
        },
        modifier = modifier)
}

@Composable
fun PasswordInputField(modifier: Modifier) {

    var password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }


    OutlinedTextField(value = password.value,

        onValueChange = {password.value = it},
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 1,
        label = {
            Text(text = stringResource(id = R.string.enter_password))
        },
        leadingIcon = {
            Icon(painterResource(id = R.drawable.password) , contentDescription = "Password",
                modifier = Modifier.size(22.dp),tint = White)

        },
        trailingIcon = {

            IconButton(onClick = {passwordVisible.value = !passwordVisible.value}) {
                Icon(painterResource(id =  if(passwordVisible.value) R.drawable.hidepass else R.drawable.showpass),
                    contentDescription = if (passwordVisible.value) stringResource(id = R.string.hidePassword) else stringResource(
                        id = R.string.showPassword
                    ),
                    modifier = Modifier.size(22.dp),tint = White)
            }
        },
        visualTransformation = if(passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier)

}