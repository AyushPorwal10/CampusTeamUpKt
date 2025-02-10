package com.example.campus_teamup.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.MainActivity
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.myactivities.UserManager
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.SplashViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun OnboardingScreen(
    auth: FirebaseAuth,
    userManager: UserManager,
    splashViewModel: SplashViewModel,
    emailLink: String,
    context: Context,
    navigateToLoginSignUpScreen: () -> Unit = {}
) {

    val showProgressBar = remember { mutableStateOf(false) }
    Log.d("Signup", "Welcome to onboarding screen")
    PerformSavingOperation(auth, userManager, splashViewModel, emailLink, context,showProgressBar)





    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
    ) {

        val (progressBar, appNameBox, imageOfThinkingBox, discoverTalentBtn) = createRefs()

        Image(painter = painterResource(id = R.drawable.onboarding),
            contentDescription = "onboardingscreen",
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(0.8f)
                .constrainAs(imageOfThinkingBox) {
                    top.linkTo(parent.top, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        if(showProgressBar.value){
            ProgressIndicator.showProgressBar(modifier = Modifier.constrainAs(progressBar){
                top.linkTo(imageOfThinkingBox.bottom, margin = 15.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },showProgressBar.value)
        }
        else {
            OutlinedButton(
                onClick = { navigateToLoginSignUpScreen() },
                modifier = Modifier.constrainAs(discoverTalentBtn) {
                    top.linkTo(imageOfThinkingBox.bottom, margin = 15.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Text(
                    text = "Discover Talents , Build Team",
                    color = White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun PerformSavingOperation(
    auth: FirebaseAuth,
    userManager: UserManager,
    splashViewModel: SplashViewModel,
    emailLink: String,
    context: Context,
    showProgressBar: MutableState<Boolean>,

    ) {
    Log.d("Signup", "Performing Saving Operation")
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        Log.d("Signup", "Launch effect launched")
        val email = userManager.userData.first().email



        if (auth.isSignInWithEmailLink(emailLink)) {
            showProgressBar.value = true

            auth.signInWithEmailLink(email, emailLink)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        coroutineScope.launch {
                            splashViewModel.checkUserDataInDataStore()
                            splashViewModel.saveEmail()
                            Log.d("Signup", "Email saving Done going to save college")
                            splashViewModel.saveUserIdToCollege()
                            Log.d("Signup", "Done with saving userid to college")

                            //after all above three operations user can navigate to home screen

                            withContext(Dispatchers.Main) {
                                Log.d("Signup", "Going to Home screen")
                                val activity = context as Activity
                                activity.startActivity(Intent(context, MainActivity::class.java))
                                activity.finish()
                            }

                        }


                    } else {
                        showProgressBar.value = false
                        Log.d("Signup", task.exception.toString())
                        ToastHelper.showToast(context, "Something went wrong")
                    }
                }
        }
    }
}
