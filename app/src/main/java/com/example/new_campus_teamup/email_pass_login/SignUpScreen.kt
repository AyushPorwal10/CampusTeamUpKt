package com.example.new_campus_teamup.email_pass_login

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.CheckEmptyFields
import com.example.new_campus_teamup.helper.ProgressIndicator
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myAnimation.FloatingBubbles
import com.example.new_campus_teamup.myAnimation.TextAnimation
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.myactivities.MainActivity
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.ButtonColor
import com.example.new_campus_teamup.ui.theme.IconColor
import com.example.new_campus_teamup.ui.theme.White
import kotlinx.coroutines.flow.Flow
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

    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "progress"
    )

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
            .background(
                brush = Brush.verticalGradient(
                    BackgroundGradientColor,
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            ),

    ) {

        FloatingBubbles()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))


            AnimatedVisibility(
                visible = animatedProgress > 0.6f,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(800, easing = EaseOutCubic)
                ) + fadeIn(animationSpec = tween(800))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.app_logo),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Welcome back",
                        color = Color.Black,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Welcome to Campus TeamUp",
                        color = Color.Black.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                    // TextAnimation.AnimatedText(modifier = Modifier)
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
            Card(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
            )
            {

                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = stringResource(id = R.string.signup_here),
                        modifier = Modifier,
                        color = Color(0xFF2D3748),
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    NameInputField(
                        modifier = Modifier

                            .fillMaxWidth(), name
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    // college name
                    CollegeNameField(
                        modifier = Modifier

                            .fillMaxWidth(), collegeName
                    )
                    Spacer(modifier = Modifier.height(12.dp))



                    EmailInputField(
                        modifier = Modifier

                            .fillMaxWidth(), email
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    PasswordField(
                        modifier = Modifier

                            .fillMaxWidth(), password, showPassword
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // signUpButton


                    Button(
                        onClick = {
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
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = ButtonColor
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading.value) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "Sign-Up",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(24.dp))


                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have account ? ",
                            color = Color(0xFF718096),
                            fontSize = 14.sp
                        )
                        // login if already have account
                        LoginButtonIfAlreadyAccount(
                            modifier = Modifier,
                            navController
                        )
                    }
                }
            }
        }
        }
    }


@Composable
fun LoginButtonIfAlreadyAccount(modifier: Modifier, navController: NavController) {



    Text(
        text = stringResource(id = R.string.login),
        color = Color(0xFF667eea),
        fontWeight = FontWeight.SemiBold,
        modifier = modifier.clickable {
            navController.navigate("login"){
                popUpTo("signup") {inclusive = false}
                launchSingleTop = true
            }
        }
    )
}
@Composable
fun EmailInputField(modifier: Modifier, email: MutableState<String>) {
    OutlinedTextField(
        value = email.value, onValueChange = {
            email.value = it

        },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines =2,
        label = {
            Text(text = stringResource(id = R.string.enter_email))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.email),
                contentDescription = stringResource(R.string.email),
                modifier = Modifier.size(22.dp),
                tint = IconColor
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
        maxLines = 2,
        label = {
            Text(text = stringResource(id = R.string.enter_name))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.profile),
                contentDescription = stringResource(R.string.user_profile_icon),
                modifier = Modifier.size(22.dp),
                tint = IconColor
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
                tint = IconColor
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
                tint = IconColor
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
                tint = IconColor
            )
        },
        visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier
    )
}
