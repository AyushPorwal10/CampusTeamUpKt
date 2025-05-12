package com.example.new_campus_teamup.myotp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpLogin : ComponentActivity() {

    private val signUpLoginViewModel : SignUpLoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "login"){
                composable("login") {
                    LoginWithOtp(signUpLoginViewModel = signUpLoginViewModel, context = this@SignUpLogin, navController)
                }
                composable("signup"){
                    UserSignUpScreen(signUpLoginViewModel , navController , this@SignUpLogin)
                }
                composable("otpverification/{loginOrSignUp}/{phoneNumber}") {backStackEntry->
                    val isLoginOrSignUp = backStackEntry.arguments?.getString("loginOrSignUp") ?: ""
                    val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""

                    Log.d("OneTimePass","User want to $isLoginOrSignUp")
                    OtpVerificationScreen(phoneNumber , signUpLoginViewModel , isLoginOrSignUp)
                }
            }
        }
    }
}