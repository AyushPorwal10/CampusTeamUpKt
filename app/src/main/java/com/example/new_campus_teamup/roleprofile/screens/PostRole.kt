package com.example.new_campus_teamup.roleprofile.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.ProgressIndicator
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myAnimation.FloatingBubbles
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BackgroundGradientColor
import com.example.new_campus_teamup.ui.theme.Black
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.ButtonColor
import com.example.new_campus_teamup.ui.theme.IconColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.CreatePostViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostRole(createPostViewModel: CreatePostViewModel) {
    val context = LocalContext.current

    val isLoading = createPostViewModel.isLoading.collectAsState()
    var role by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        createPostViewModel.postUiEvent.collect { message->
            role = ""
            ToastHelper.showToast(context , message)
        }
    }
    LaunchedEffect(Unit){
        createPostViewModel.errorMessage.collect{error->
            error?.let {
                ToastHelper.showToast(context ,error)
                createPostViewModel.clearError()
            }
        }
    }

    val placeholders = listOf(
        "Android App Developer", "IOS App Developer",
        "Web Fronted Developer",
        "Flutter Developer",
        "Web Backend Developer",
        "Web Full Stack Developer", ""
    )


    var currentPlaceholderIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentPlaceholderIndex = (currentPlaceholderIndex + 1) % placeholders.size
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
                .padding(24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


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
                        text = "Post Role",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                    Spacer(modifier = Modifier.height(32.dp))


                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .fillMaxWidth()
                        .background(Color(0xFFEFEEFF))) {
                        Text(
                            text = stringResource(id = R.string.note_for_making_post),
                            color = Black,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(12.dp)
                        )
                    }




                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(value = role,
                        onValueChange = { role = it },
                        colors = TextFieldStyle.myTextFieldColor(),
                        shape = TextFieldStyle.defaultShape,
                        maxLines = 2,
                        placeholder = {
                            Box(
                                modifier = Modifier.animateContentSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = placeholders[currentPlaceholderIndex],
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                        },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.roles), contentDescription = "Email Icon",
                                modifier = Modifier.size(22.dp), tint = IconColor
                            )
                        }, modifier = Modifier
                            .fillMaxWidth())


                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = {
                            if (role.isEmpty()) {
                                ToastHelper.showToast(context, "Please Enter a role")
                            } else {
                                Log.d("PostRole", "Post Button Clicked ${LocalDate.now()}")
                                createPostViewModel.postRole(
                                    role,
                                    LocalDate.now().toString())

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
                                    text = "Post",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                }
            }
        }
    }
}