package com.example.campus_teamup.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White
import kotlinx.coroutines.delay


@Composable
fun PostRole() {
    ConstraintLayout {
        val (noteForRole , enterRole , postRoleBtn) = createRefs()

        var role by remember { mutableStateOf("") }
        val placeholders = listOf(
            "Android App Developer","IOS App Developer",
            "App Full Stack",
            "Web Fronted Developer",
            "Web Backend Developer","Web Full Stack","")

        var currentPlaceholderIndex by remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(2000)
                currentPlaceholderIndex = (currentPlaceholderIndex + 1) % placeholders.size
            }
        }

        Box(modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp)
            .constrainAs(noteForRole) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }){
            Text(text = stringResource(id = R.string.note_for_making_post),
                color = White,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold)
        }

        OutlinedTextField(value = role,
            onValueChange = { role = it },
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            maxLines = 1,
            label = {
                Text(text = stringResource(id = R.string.enter_role))
            },
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


        OutlinedButton(onClick = {
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = BackGroundColor,
                contentColor = White
            ), modifier = Modifier.constrainAs(postRoleBtn){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(enterRole.bottom , margin = 10.dp)
            })
        {
            Text(
                text = stringResource(id = R.string.post_role),
                color = White
            )
        }
    }

}