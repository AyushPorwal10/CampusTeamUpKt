package com.example.campus_teamup.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.CreatePostViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostRole(createPostViewModel: CreatePostViewModel) {
    val context = LocalContext.current

    val isLoading = createPostViewModel.isLoading.collectAsState()


    ConstraintLayout {
        val (noteForRole, divider, progressBar, enterRole, postRoleBtn) = createRefs()

        var role by remember { mutableStateOf("") }
        val placeholders = listOf(
            "Android App Developer", "IOS App Developer",
            "Web Fronted Developer",
            "Flutter Developer",
            "Web Backend Developer",
            "Web Full Stack Developer", ""
        )

        if (isLoading.value)
            role = ""

        var currentPlaceholderIndex by remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(3000)
                currentPlaceholderIndex = (currentPlaceholderIndex + 1) % placeholders.size
            }
        }

        HorizontalDivider(modifier = Modifier
            .constrainAs(divider) {

            }
            .width(1.dp)
            .fillMaxWidth()
            .background(BorderColor))

    Box(modifier = Modifier
        .fillMaxWidth(0.9f)
        .padding(10.dp)
        .background(BorderColor)
        .border(0.5.dp, BorderColor, shape = RoundedCornerShape(22.dp))
        .constrainAs(noteForRole) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(divider.bottom, margin = 10.dp)
        }) {
        Text(
            text = stringResource(id = R.string.note_for_making_post),
            color = White,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
    }

    OutlinedTextField(value = role,
        onValueChange = { role = it },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape,
        maxLines = 1,
        placeholder = {
            Box(
                modifier = Modifier.animateContentSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = placeholders[currentPlaceholderIndex],
                    style = MaterialTheme.typography.titleMedium,
                    color = White
                )
            }
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.roles), contentDescription = "Email Icon",
                modifier = Modifier.size(22.dp), tint = White
            )
        }, modifier = Modifier
            .constrainAs(enterRole) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(noteForRole.bottom, margin = 10.dp)
            }
            .fillMaxWidth(0.9f))



    if (isLoading.value) {
        ProgressIndicator.showProgressBar(
            modifier = Modifier.constrainAs(progressBar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(enterRole.bottom, margin = 10.dp)
            },
            true
        )
    } else {
        OutlinedButton(onClick = {
            if (role.isEmpty()) {
                ToastHelper.showToast(context, "Please Enter a role")
            } else {
                Log.d("PostRole", "Post Button Clicked ${LocalDate.now()}")
                createPostViewModel.postRole(role, LocalDate.now().toString())
            }
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = BackGroundColor,
                contentColor = White
            ), modifier = Modifier.constrainAs(postRoleBtn) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(enterRole.bottom, margin = 10.dp)
            })
        {
            Text(
                text = stringResource(id = R.string.post_role),
                color = White
            )
        }
    }

}

}