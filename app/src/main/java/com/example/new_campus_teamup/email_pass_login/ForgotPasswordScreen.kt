package com.example.new_campus_teamup.email_pass_login


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.CheckEmptyFields
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myAnimation.FloatingBubbles
import com.example.new_campus_teamup.myAnimation.TextAnimation
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.ButtonColor
import com.example.new_campus_teamup.ui.theme.IconColor
import com.example.new_campus_teamup.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    loginSignUpViewModel: LoginSignUpViewModel,
    navController: NavHostController,
) {
    val email = remember { mutableStateOf("") }
    var emailSentDialog by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "progress"
    )

    val isLoading = loginSignUpViewModel.isLoading.collectAsState()

    val context = LocalContext.current
    val textColor = White

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(emailSentDialog) {
        if (emailSentDialog) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Email sent successfully",
                    actionLabel = "OK"
                )
            }
            emailSentDialog = false
        }
    }
    LaunchedEffect(Unit) {
        loginSignUpViewModel.errorMessage.collect { error ->
            error?.let {
                ToastHelper.showToast(context, error)
                loginSignUpViewModel.resetErrorMessage()
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = BackgroundGradientColor,
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )

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

                Spacer(modifier = Modifier.height(60.dp))

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
                                painter = painterResource(R.drawable.campus_teamup_circle),
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
                //  Email input field

                Spacer(modifier = Modifier.height(60.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.95f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {



                        Text(text = "Password reset instructions will be sent to your entered email.",modifier = Modifier.fillMaxWidth() ,
                            fontWeight = FontWeight.SemiBold , textAlign = TextAlign.Center)

                        Spacer(modifier = Modifier.height(30.dp))

                        ChangePasswordEmailInput(
                            modifier = Modifier

                                .fillMaxWidth(), email
                        )


                        Spacer(modifier = Modifier.height(40.dp))

                        Button(
                            onClick = {
                                if (!CheckEmptyFields.isValidEmail(email.value.trim())) {
                                    ToastHelper.showToast(context, "Please Enter valid email")
                                } else {
                                    loginSignUpViewModel.isEmailRegistered(
                                        email.value,
                                        isRegistered = {
                                            if (it) {
                                                loginSignUpViewModel.sendPasswordResetEmail(
                                                    email.value,
                                                    isEmailSent = {
                                                        emailSentDialog = true
                                                    })
                                            }
                                        })
                                }
                            }, modifier = Modifier
                                .fillMaxWidth(0.7f)
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
                                    text = stringResource(R.string.forgotPassword),
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                 }
                            }
                        }
                         }

                        // login sign up button

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            LoginButton(modifier = Modifier, onClick = {
                                navController.navigate("login") {
                                    popUpTo("forgotpassword") { inclusive = false }
                                    launchSingleTop = true
                                }
                            })
                        }

                    }
                }

            }
        }
}

@Composable
fun LoginButton(modifier: Modifier, onClick: () -> Unit) {
    TextButton(onClick = {
        onClick()
    }) {
        Text(
            text = stringResource(R.string.login),
            color = Color(0xFF667eea),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ChangePasswordEmailInput(modifier: Modifier, email: MutableState<String>) {
    OutlinedTextField(
        value = email.value,
        onValueChange = { email.value = it },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 2,
        label = {
            Text(text = stringResource(id = R.string.enter_email))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.email), contentDescription = "Phone Icon",
                modifier = Modifier.size(22.dp), tint = IconColor
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

