package com.example.new_campus_teamup.email_pass_login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginSignUp : ComponentActivity() {
    private val loginSignUpViewModel : LoginSignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()
            val navController = rememberNavController()
            NavHost(navController, startDestination = "login"){
                composable("login") {
                    LoginScreen(loginSignUpViewModel , context = this@LoginSignUp, navController)
                }
                composable("signup"){
                    SignUpScreen(loginSignUpViewModel , navController , this@LoginSignUp)
                }
                composable("forgotpassword") {
                    ForgotPasswordScreen(loginSignUpViewModel, navController)
                }

            }
        }
    }
}