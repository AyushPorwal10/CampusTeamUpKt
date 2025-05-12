package com.example.new_campus_teamup.myotp


import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.helper.ProgressIndicator
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myactivities.MainActivity
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.LightTextColor
import com.example.new_campus_teamup.ui.theme.White


@Composable
fun OtpVerificationScreen(
    phoneNumber: String,
    signUpLoginViewModel: SignUpLoginViewModel,
    isLoginOrSignUP : String ,
) {
    val otpLength = 6
    var otpCode by remember { mutableStateOf("") }
    var timeLeft by remember { mutableStateOf(60) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val isOtpSent = signUpLoginViewModel.isOptSent.collectAsState()

    val isOtpVerifying = signUpLoginViewModel.isOptVerifying.collectAsState()

    LaunchedEffect(Unit){
        signUpLoginViewModel.errorMessage.collect{error->
            error?.let {
                ToastHelper.showToast(context ,error)
                signUpLoginViewModel.resetErrorMessage()
            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        
        Box(modifier = Modifier.size(100.dp)){
            LoadAnimation(modifier = Modifier , R.raw.otp , playAnimation = true)
        }

        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = "Enter the 6-digit OTP",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = White
        )

        Spacer(modifier = Modifier.height(8.dp))
        if(isOtpSent.value){
            Text(
                text = "We’ve sent a verification code to your phone\nnumber $phoneNumber",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = LightTextColor
            )
        }
        else{
            Text(
                text = "Please wait while we are sending OTP \nto your number $phoneNumber",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = LightTextColor
            )
        }


        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OtpTextField(otpText = otpCode, onOtpTextChange = {value, otpInputFilled ->
                otpCode = value
            } )
        }

        Spacer(modifier = Modifier.height(24.dp))


        if(isOtpVerifying.value || !isOtpSent.value){
            ProgressIndicator.showProgressBar(modifier = Modifier, canShow =isOtpVerifying.value)
        }
        else {
            if(otpCode.length == otpLength && isOtpSent.value){

                OutlinedButton(
                    onClick = {
                        signUpLoginViewModel.verifyOtpAndSignIn(otpCode, onSuccess = {

                            // if user while sign up do opt verified than save its data to db
                            if(isLoginOrSignUP == "signup") {
                                signUpLoginViewModel.saveUserDataToDatabase(phoneNumber , onDataSaved = {
                                    ToastHelper.showToast(context, "OTP Verified")
                                    // Browse to main activitity
                                    val intent = Intent(context , MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(intent)
                                })

                            }
                            else{
                                // in case of login fetch data
                                signUpLoginViewModel.fetchDataFromDatabase(phoneNumber , onUserDataSaved = {
                                    ToastHelper.showToast(context, "OTP Verified")
                                    val intent = Intent(context , MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(intent)
                                })

                            }


                        }, onError = {

                            when {
                                it.contains("invalid", ignoreCase = true) && it.contains("code", ignoreCase = true) -> {
                                    ToastHelper.showToast(context, "Invalid OTP. Please check and try again.")
                                }
                                else -> {
                                    ToastHelper.showToast(context, "Something went wrong!")
                                }
                            }

                        })
                    },
                    modifier = Modifier.fillMaxWidth(0.6f),
                ) {
                    Text(text = "Verify", color = White)
                }
            }
        }


        Spacer(modifier = Modifier.height(12.dp))



//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "Resend OTP in 00:${timeLeft.toString().padStart(2, '0')}",
//                fontSize = 10.sp,
//                color = LightTextColor
//            )
//
//            if (timeLeft == 0) {
//                TextButton(
//                    onClick = {
//                        timeLeft = 60
//                    }
//                ) {
//                    Text("Resend OTP", color = White)
//                }
//            }
//        }


    }
}


@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 6,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpCount) {
            throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
        }
    }

    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center , verticalAlignment = Alignment.CenterVertically) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }
    Box(
        modifier = Modifier
            .width(44.dp)
            .height(54.dp)
            .border(
                1.dp,
                if (isFocused) White else Color.Gray,
                RoundedCornerShape(8.dp)
            )
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            style = MaterialTheme.typography.titleLarge,
            color = if (isFocused) Color.Gray else White,
            textAlign = TextAlign.Center
        )
    }
}