package com.example.campus_teamup.chatsection.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campus_teamup.R
import com.example.campus_teamup.myactivities.Chat
import com.example.campus_teamup.mydataclass.SendMessage
import com.example.campus_teamup.roleprofile.screens.CodingProfilesBtn
import com.example.campus_teamup.roleprofile.screens.CollegeDetailsBtn
import com.example.campus_teamup.roleprofile.screens.SkillSectionBtn
import com.example.campus_teamup.roleprofile.screens.ViewCodingProfiles
import com.example.campus_teamup.roleprofile.screens.ViewCollegeDetails
import com.example.campus_teamup.roleprofile.screens.ViewSkills
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.ChatViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatViewModel: ChatViewModel = hiltViewModel()) {

    var messageText by remember{
        mutableStateOf("")
    }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "User Name", color = White
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackGroundColor, navigationIconContentColor = White
        ),
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.browseback),
                        contentDescription = null,
                        tint = White
                    )
                }
            })
    }) { paddingValues ->

        ConstraintLayout(
            modifier = Modifier
                .background(BackGroundColor)
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            val ( divider) = createRefs()


            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BorderColor)
                    .constrainAs(divider) {

                    })
            
            
            Column {
                OutlinedTextField(value = messageText, onValueChange = { text ->
                    messageText = text
                })

                OutlinedButton(onClick = {
                    chatViewModel.sendMessage(
                        SendMessage("ayush1010", "2:34:49", "Hi bro"),
                        "ayush1010aman999"
                    )
                }) {
                    Text(text = "Send" , color = White)
                }
            }
        }
    }

}