package com.example.campus_teamup.screens

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White


@Composable
fun PostVacancy() {

    val scrollState = rememberScrollState()
    val teamName = remember { mutableStateOf("") }
    val roleLookingFor = remember{ mutableStateOf("") }
    val hackathonName = remember { mutableStateOf("") }
    val skills = remember { mutableStateOf("") }
    val roleDescription = remember{ mutableStateOf("") }


    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        // Team Name

        OutlinedTextField(value = teamName.value,
            onValueChange = { teamName.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            maxLines = 2,
            label = {
                Text(text = stringResource(id = R.string.enter_team_name))
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.email), contentDescription =null,
                    modifier = Modifier.size(22.dp), tint = White
                )
            }
        )

        // Hackathon Name

        OutlinedTextField(value = hackathonName.value,
            onValueChange = { hackathonName.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            maxLines = 2,
            label = {
                Text(text = stringResource(id = R.string.hackathon_name))
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.email), contentDescription =null,
                    modifier = Modifier.size(22.dp), tint = White
                )
            }
        )



        // Role Looking For
        OutlinedTextField(value = roleLookingFor.value,
            onValueChange = { roleLookingFor.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            maxLines = 2,
            label = {
                Text(text = stringResource(id = R.string.role_looking_for))
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.profile), contentDescription = null,
                    modifier = Modifier.size(22.dp), tint = White
                )
            }
        )


        // Skills Required

        OutlinedTextField(value = skills.value,
            onValueChange = { skills.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            maxLines = 2,
            label = {
                Text(text = stringResource(id = R.string.skills_needed))
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.profile), contentDescription = null,
                    modifier = Modifier.size(22.dp), tint = White
                )
            }
        )


        OutlinedTextField(value = roleDescription.value,
            onValueChange = { roleDescription.value = it },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldStyle.myTextFieldColor(),
            shape = TextFieldStyle.defaultShape,
            maxLines = 2,
            label = {
                Text(text = stringResource(id = R.string.describe_role))
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.email), contentDescription =null,
                    modifier = Modifier.size(22.dp), tint = White
                )
            }
        )


        OutlinedButton(onClick = {

        },
            colors = ButtonDefaults.buttonColors(
                containerColor = BackGroundColor,
                contentColor = White
            ))
        {
            Text(
                text = stringResource(id = R.string.post_role),
                color = White
            )
        }


    }
}