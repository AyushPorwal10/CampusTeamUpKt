package com.example.campus_teamup.myactivities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.campus_teamup.MainActivity
import com.example.campus_teamup.mysealedClass.BottomNavScreens
import com.example.campus_teamup.mysealedClass.LoginSignupScreens
import com.example.campus_teamup.screens.LoginNavTesting
import com.example.campus_teamup.screens.LoginScreen
import com.example.campus_teamup.screens.SignUpScreen

import com.example.campus_teamup.ui.theme.BackGroundColor

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController,
                startDestination = LoginSignupScreens.Login.screen
            ){
                composable(LoginSignupScreens.Login.screen) {
                    LoginScreen(
                        navigateToSignUpScreen = {
                            navController.navigate(LoginSignupScreens.SignUp.screen){
                                popUpTo(LoginSignupScreens.Login.screen) {inclusive = true}
                            }
                        },
                        navigateToHomeScreen = {
                            startActivity(Intent(this@Login , MainActivity::class.java))
                            finish()
                        }
                    )
                }

                composable(LoginSignupScreens.SignUp.screen) {
                    SignUpScreen(
                        navigateToHomeScreen = {
                            startActivity(Intent(this@Login , MainActivity::class.java))
                            finish()
                        }
                    )
                }
            }

        }
    }
}
