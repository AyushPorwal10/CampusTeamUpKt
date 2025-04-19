package com.example.campus_teamup.chatsection.screens

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.TimeAndDate
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.mydataclass.SendMessage
import com.example.campus_teamup.roleprofile.screens.ViewSkills
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.ChatViewModel
import com.google.firebase.Timestamp


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(senderName: String? ,chatRoomId : String? ,  currentUserId : String? , chatViewModel: ChatViewModel = hiltViewModel()) {

    val chatHistory = chatViewModel.chatHistory.collectAsState()
    val chatHistoryState = rememberLazyListState()
    val activity = LocalContext.current as? Activity
    LaunchedEffect(chatHistory.value){
        chatViewModel.fetchChatHistory(chatRoomId!!)
    }
    val context = LocalContext.current
    Log.d("ChatHistory","Current user id is $currentUserId <-  ChatRoomId is $chatRoomId <-  SenderName is $senderName")
    var messageText by remember {
        mutableStateOf("")
    }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = senderName + "", color = White
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackGroundColor, navigationIconContentColor = White
        ),
            navigationIcon = {
                IconButton(onClick = {
                    activity?.finish()
                }) {
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

            val (divider, typeAndSendMessage , chatHistoryArea) = createRefs()


            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BorderColor)
                    .constrainAs(divider) {

                    })

            LazyColumn(
                state = chatHistoryState,
                modifier = Modifier
                    .padding(paddingValues)
                    .constrainAs(chatHistoryArea) {
                        top.linkTo(divider.bottom)
                        bottom.linkTo(typeAndSendMessage.top, margin = 8.dp)
                    }
                    .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)){
                items(chatHistory.value){messageDetails->
                    ShowChats(messageDetails = messageDetails, currentUserId = currentUserId)
                }
            }
            LaunchedEffect(chatHistory.value.size){
                if(chatHistory.value.isNotEmpty()){
                    chatHistoryState.animateScrollToItem(chatHistory.value.size-1)
                }
            }

            Row(modifier = Modifier
                .constrainAs(typeAndSendMessage) {
                    bottom.linkTo(parent.bottom, margin = 8.dp)

                }
                .padding(6.dp)) {

                TextField(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(4.dp)
                        .border(1.dp, BorderColor, RoundedCornerShape(22.dp)),
                    value = messageText, onValueChange = { text ->
                        messageText = text
                    },
                    placeholder = {
                                  Text(text = "Message", color = LightTextColor)
                    },
                     colors = TextFieldStyle.myTextFieldColor()
                )

                if(messageText.trim().isNotEmpty()){
                    IconButton(onClick = {
                        if (chatRoomId != null) {
                            chatViewModel.sendMessage(SendMessage(
                                currentUserId!!,
                                TimeAndDate.formatDate(System.currentTimeMillis()),
                                messageText,
                            ),chatRoomId , onMessageSent = {
                            },
                                errorSendingMessage = {
                                    ToastHelper.showToast(context , "Something went wrong")
                                })
                        }
                        messageText = ""
                    },modifier = Modifier.padding(4.dp)) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null , tint = White)
                    }
                }


            }
        }
    }

}