package com.example.campus_teamup.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.R
//import com.example.campus_teamup.myAnimation.TextAnimation
import com.example.campus_teamup.myThemes.PrimaryBlack
import com.example.campus_teamup.myThemes.PrimaryWhiteGradient
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.White

@Composable
fun SignUpScreen(navigateToHomeScreen : ()-> Unit) {
    val theme = isSystemInDarkTheme()
    val textColor = if (theme) White else Black
    val appLogo = if (theme) R.drawable.dark_theme_logo else R.drawable.white_theme_logo
    val backgroundColor = if (theme) Black else White

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(1f)
    ) {
        SignUpHeader( appLogo)
        SignUpBox(textColor, backgroundColor) { navigateToHomeScreen() }
    }


}

@Composable
fun SignUpHeader( appLogo: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(0.dp, 40.dp)
    ) {
        Image(
            painter = painterResource(id = appLogo),
            contentDescription = "", modifier = Modifier
                .clip(CircleShape)
                .size(100.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))


        //TextAnimation.AnimatedText()

    }
}


@Composable
fun SignUpBox(textColor: Color, backgroundColor: Color, navigateToHomeScreen: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var collegeName by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(
                if (isSystemInDarkTheme())
                    PrimaryBlack
                else
                    PrimaryWhiteGradient
            )
            .border(1.dp, color = Black, shape = RoundedCornerShape(22.dp))


            .padding(15.dp, 25.dp, 15.dp, 25.dp)


    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.login_here),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = email,
                onValueChange = { emailInput -> email = emailInput },
                colors = TextFieldStyle.myTextFieldColor(),
                shape = TextFieldStyle.defaultShape,
                label = {
                    Text("Enter Email ", style = TextStyle(color = textColor))
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.email), contentDescription = "Email Icon",
                        modifier = Modifier.size(22.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { passInput -> password = passInput },
                colors = TextFieldStyle.myTextFieldColor(),
                shape = TextFieldStyle.defaultShape,
                label = {
                    Text("Enter Password ", style = TextStyle(color = textColor))
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.password), contentDescription = "Pass Icon",
                        modifier = Modifier.size(22.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painterResource(if (passwordVisible) R.drawable.hidepass else R.drawable.showpass),
                            contentDescription = if (passwordVisible) stringResource(id = R.string.hidePassword) else stringResource(
                                id = R.string.showPassword
                            ), modifier = Modifier.size(22.dp)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { nameInput -> name = nameInput },
                colors = TextFieldStyle.myTextFieldColor(),
                shape = TextFieldStyle.defaultShape,
                label = {
                    Text("Enter Your Name ", style = TextStyle(color = textColor))
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.profile), contentDescription = "Profile Icon",
                        modifier = Modifier.size(22.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = collegeName,
                onValueChange = { collegeNameInput -> collegeName = collegeNameInput },
                colors = TextFieldStyle.myTextFieldColor(),
                shape = TextFieldStyle.defaultShape,
                label = {
                    Text("Enter Your Name ", style = TextStyle(color = textColor))
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.college), contentDescription = "College Icon",
                        modifier = Modifier.size(22.dp)
                    )
                }
            )


            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(onClick = { navigateToHomeScreen() }, modifier = Modifier.fillMaxWidth(0.4f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(isSystemInDarkTheme()) Black else White,
                    contentColor = textColor
                )

            ) {
                Text(
                    text = stringResource(id = R.string.signUp),
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor
                )
            }



            Spacer(modifier = Modifier.height(20.dp))
            LoginButton(textColor)


        }
    }

}

@Composable
private fun LoginButton(textColor: Color) {
    Row(
        horizontalArrangement = Arrangement.Absolute.SpaceEvenly
    ) {

        Text(
            text = stringResource(id = R.string.Login),
            style = MaterialTheme.typography.titleSmall.copy(
                textDecoration = TextDecoration.Underline
            ),
            color = textColor,
            modifier = Modifier.fillMaxWidth(0.5f)
        )
    }
}
