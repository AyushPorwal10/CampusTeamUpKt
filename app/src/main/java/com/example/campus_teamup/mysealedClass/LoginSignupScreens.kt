package com.example.campus_teamup.mysealedClass

sealed class LoginSignupScreens (val screen : String){
    data object Login : LoginSignupScreens("login")
    data object SignUp : LoginSignupScreens("signUp")

}