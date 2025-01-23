package com.example.campus_teamup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White


@Preview
@Composable
fun LoginRedesign(
    navigateToSignUpScreen : () -> Unit = {},
    navigateToHomeScreen : () -> Unit = {}
) {



    val textColor = White

    Box(modifier = Modifier
        .fillMaxSize()
        .background(BackGroundColor) , contentAlignment = Alignment.Center ){

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (appLogo , welComeText , loginHeading , emailField , passwordField , loginButton , forgotPassword , signUp) = createRefs()

            Image(
                painterResource(id = R.drawable.app_logo)  ,  contentDescription = stringResource(
                id = R.string.app_name
            ) , modifier = Modifier.constrainAs(appLogo){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top , margin = 20.dp)
                }.size(100.dp) )

            Text(
                text = stringResource(id = R.string.welcomeGreeting) ,
                color = textColor ,
                modifier = Modifier.constrainAs(welComeText){
                top.linkTo(appLogo.bottom , margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
            } , style = MaterialTheme.typography.titleMedium)

            Text(
                text = stringResource(id = R.string.login_here),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.constrainAs(loginHeading){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(welComeText.bottom , margin = 40.dp)
                },
                color = textColor,
                fontWeight = FontWeight.Bold

            )


            // Email and Login Fields


            // email

            LoginEmailInputField(modifier = Modifier.constrainAs(emailField){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(loginHeading.bottom , margin = 20.dp)
            })

            
            
            // for password

            LoginPasswordInputField(modifier = Modifier.constrainAs(passwordField){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(emailField.bottom , margin = 8.dp)
            })



            LoginButton(modifier = Modifier.constrainAs(loginButton){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(passwordField.bottom, margin = 20.dp)
            }.clickable { navigateToHomeScreen() })





            // forgot password

            ForgotPassword(modifier = Modifier.constrainAs(forgotPassword){
                start.linkTo(parent.start)
                top.linkTo(loginButton.bottom , margin = 30.dp)
            })



            // new account sign up button
            NewAccountSignUpButton(modifier = Modifier.constrainAs(signUp){
                end.linkTo(parent.end )
                top.linkTo(loginButton.bottom , margin = 30.dp)
            }.clickable { navigateToSignUpScreen() })


            createHorizontalChain(forgotPassword , signUp , chainStyle = ChainStyle.Spread)


        }
    }

}

@Composable
fun NewAccountSignUpButton(modifier: Modifier) {
    Text(text = stringResource(id = R.string.signUp),
        color = White , modifier = modifier)
}

@Composable
fun ForgotPassword(modifier: Modifier) {

    Text(text = stringResource(id = R.string.forgotPassword),
        color = White , modifier = modifier)

}

@Composable
fun LoginButton(modifier: Modifier) {

    OutlinedButton(onClick = { /*TODO*/ } ,
        colors = ButtonDefaults.buttonColors(
            containerColor = BackGroundColor,
            contentColor = White
        ), modifier = modifier)
    {
        Text(text = stringResource(id = R.string.Login),
            color = White)
    }

}

@Composable
fun LoginPasswordInputField(modifier: Modifier) {

    var password = remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }



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
                modifier = Modifier.size(22.dp) ,
                tint = White)

        },
        trailingIcon = {

            IconButton(onClick = {passwordVisible = !passwordVisible}) {
                Icon(painterResource(id =  if(passwordVisible) R.drawable.hidepass else R.drawable.showpass),
                    contentDescription = if (passwordVisible) stringResource(id = R.string.hidePassword) else stringResource(
                        id = R.string.showPassword
                    ),
                    modifier = Modifier.size(22.dp),tint = White)
            }
        },
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier
        )
}

@Composable
fun LoginEmailInputField(modifier: Modifier) {

    var email = remember { mutableStateOf("") }

    OutlinedTextField(value = email.value,
        onValueChange = { email.value = it},
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 1,
        label = {
            Text(text = stringResource(id = R.string.enter_email))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.email), contentDescription = "Email Icon",
                modifier = Modifier.size(22.dp),tint = White
            )
        }
        , modifier = modifier)
}
