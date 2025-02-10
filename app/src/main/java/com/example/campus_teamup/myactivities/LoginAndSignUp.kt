package com.example.campus_teamup.myactivities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campus_teamup.MainActivity
import com.example.campus_teamup.mysealedClass.LoginSignupScreens
import com.example.campus_teamup.screens.LoginScreen
import com.example.campus_teamup.screens.SignUpScreen
import com.example.campus_teamup.viewmodels.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginAndSignUp : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val signUpViewModel : SignUpViewModel = hiltViewModel()
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
                            startActivity(Intent(this@LoginAndSignUp , MainActivity::class.java))
                            finish()
                        }
                    )
                }

                composable(LoginSignupScreens.SignUp.screen) {
                    SignUpScreen(
                        signUpViewModel ,
                        navigateToLoginScreen = {
                            navController.navigate(LoginSignupScreens.Login.screen){
                                popUpTo(LoginSignupScreens.SignUp.screen) {inclusive = true}
                            }
                        }
                    )
                }
            }

        }
    }
}

