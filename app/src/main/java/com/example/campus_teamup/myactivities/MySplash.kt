package com.example.campus_teamup.myactivities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campus_teamup.R
import com.example.campus_teamup.screens.OnboardingScreen
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.SplashViewModel
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


    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val emailLink = intent.data.toString()
            val context = LocalContext.current

            splashViewModel = hiltViewModel()
            SplashScreen(auth , context) {
                setContent {
                    OnboardingScreen(auth , userManager , splashViewModel,emailLink,context
                    ) {
                        startActivity(Intent(this, LoginAndSignUp::class.java))
                        finish()
                    }
                }
            }
        }

    }
}

@Composable
fun SplashScreen(auth: FirebaseAuth, context : Context, navigateToOnboardingScreen : () -> Unit) {

    var showOnBoardingScreen = remember {
        mutableStateOf(false)
    }
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
                painter = painterResource(id = R.drawable.white_theme_logo),
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
        delay(1200)
        // if user is already logged in than don't ask for login or signup

        if(auth.currentUser != null){
            val activity = context as Activity
            activity.startActivity(Intent(context, MainActivity::class.java))
            activity.finish()
        }
        navigateToOnboardingScreen()

    }
}