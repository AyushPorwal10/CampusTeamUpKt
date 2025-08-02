package com.example.new_campus_teamup.email_pass_login

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.CheckEmptyFields
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myAnimation.FloatingBubbles
import com.example.new_campus_teamup.myAnimation.TextAnimation
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.myactivities.MainActivity
import com.example.new_campus_teamup.mysealedClass.BottomNavScreens
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.ButtonColor
import com.example.new_campus_teamup.ui.theme.IconColor
import com.example.new_campus_teamup.ui.theme.White



@Composable
fun LoginScreen(
    loginSignUpViewModel: LoginSignUpViewModel,
    context: Activity,
    navController: NavHostController,
) {


    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "progress"
    )
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


    Box(modifier = Modifier.fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = BackgroundGradientColor,
                startY = 0f,
                endY = Float.POSITIVE_INFINITY
            )
        ),
    ){

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

              TextAnimation.AnimatedText(modifier = Modifier, )
         }

     }

         Spacer(modifier = Modifier.height(60.dp))

         Card(modifier = Modifier.fillMaxWidth()
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
                 Text(
                     text = "Login",
                     fontSize = 28.sp,
                     fontWeight = FontWeight.Bold,
                     color = Color(0xFF2D3748)
                 )
                 Spacer(modifier = Modifier.height(32.dp))

                 // Email Field

                 UserEmailField(
                     modifier = Modifier
                         .fillMaxWidth(), email
                 )




                 Spacer(modifier = Modifier.height(16.dp))

                 // Password Field

                 UserPasswordField(
                     modifier = Modifier

                         .fillMaxWidth(), password, showPassword
                 )

                 Spacer(modifier = Modifier.height(8.dp))

                 // Forgot Password


                 ForgotPasswordButton(modifier = Modifier.align(Alignment.End), onClick = {
                navController.navigate("forgotpassword"){
                    popUpTo("login") {inclusive = false}
                    launchSingleTop = true
                }
                 })


                 Spacer(modifier = Modifier.height(24.dp))

                 // Login Button
                 Button(
                     onClick = {
                         if (!CheckEmptyFields.isValidEmail(email.value.trim())) {
                             ToastHelper.showToast(context, "Please Enter valid email")
                         } else if (password.value.trim().isEmpty()) {
                             ToastHelper.showToast(context, "Please Enter valid password")
                         } else {
                        loginSignUpViewModel.signInUser(email.value , password.value , onSuccess = {

                            loginSignUpViewModel.fetchDataFromDatabase(loginSignUpViewModel.getUserId(email.value.trim()), onUserDataSaved = {
                                val intent = Intent(context , MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(intent)
                            })
                        })
                         }
                     },
                     modifier = Modifier
                         .fillMaxWidth()
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
                             text = "Login",
                             color = Color.White,
                             fontSize = 18.sp,
                             fontWeight = FontWeight.SemiBold
                         )
                         }
                     }
                 }

                 Spacer(modifier = Modifier.height(24.dp))

                 // Sign Up Text
                 Row(
                     horizontalArrangement = Arrangement.Center,
                     verticalAlignment = Alignment.CenterVertically
                 ) {
                     Text(
                         text = "Don't have an account? ",
                         color = Color(0xFF718096),
                         fontSize = 14.sp
                     )
                     NewAccountSignUpButton(onClick = {
                navController.navigate("signup"){
                    popUpTo("login") {inclusive = false}
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
fun NewAccountSignUpButton( onClick: () -> Unit) {
    TextButton(
        onClick = { onClick() },
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = "Sign up",
            color = Color(0xFF667eea),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ForgotPasswordButton(modifier: Modifier, onClick: () -> Unit) {



    TextButton(
        onClick = {
            onClick()
        },
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.forgotPassword),
            color = Color(0xFF667eea),
            fontSize = 14.sp
        )
    }
}


@Composable
fun UserEmailField(modifier: Modifier, email: MutableState<String>) {
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
        maxLines = 2,
        label = {
            Text(text = stringResource(id = R.string.enter_password))
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.password), contentDescription = "Password Icon",
                modifier = Modifier.size(22.dp), tint = Color(0xFF667eea)
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


