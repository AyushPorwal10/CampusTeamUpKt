package com.example.new_campus_teamup.myactivities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.email_pass_login.LoginSignUp
import com.example.new_campus_teamup.screens.OnboardingScreen
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.White
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MySplash  : ComponentActivity() {
    private val auth = Firebase.auth

    @Inject
    lateinit var firebaseFirestore: FirebaseFirestore

    @Inject
    lateinit var userManager: UserManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            enableEdgeToEdge()
            val context = LocalContext.current

            SplashScreen(auth , context) {
                setContent {
                    OnboardingScreen{
                        startActivity(Intent(this, LoginSignUp::class.java))
                        finish()
                    }
                }
            }
        }

    }
}
@Composable
fun SplashScreen(auth: FirebaseAuth, context : Context, navigateToOnboardingScreen : () -> Unit) {


    Log.d("SplashScreen","Splash Screen")
    val textColor = White
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.campus_teamup_circle),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = R.string.app_name)
                ,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium ,
                color = textColor ,
                maxLines = 1 ,)
        }

    }
    LaunchedEffect(Unit){
        delay(1000)
        if(auth.currentUser != null){
            val activity = context as Activity
            activity.startActivity(Intent(context, MainActivity::class.java))
            activity.finish()
        }
        else{
            navigateToOnboardingScreen()
        }


    }
}