package com.example.new_campus_teamup.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.helper.rememberNetworkStatus
import com.example.new_campus_teamup.mydataclass.RecentChats
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.RecentChatsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentChatScreen(startChat : (RecentChats) -> Unit){

    val recentChatViewModel : RecentChatsViewModel = hiltViewModel()
    val activity = LocalContext.current as? Activity
    recentChatViewModel.fetchRecentChats()
    val userAllChats = recentChatViewModel.userAllChats.collectAsState()
    val areChatsLoading = recentChatViewModel.areChatsLoading.collectAsState()
    val isConnected = rememberNetworkStatus()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit){
        recentChatViewModel.errorMessage.collect{error->
            error?.let {
                ToastHelper.showToast(context ,error)
                recentChatViewModel.clearError()
            }
        }
    }


    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.your_chats),
                    color = White
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = BackGroundColor,
                navigationIconContentColor = White
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
            }
        )
    }) {paddingValues ->


        LaunchedEffect(isConnected) {
            if (!isConnected) {
                snackbarHostState.showSnackbar(
                    message = "No Internet Connection",
                    actionLabel = "OK"
                )
            }
        }

        if(isConnected){
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(BackGroundColor),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(userAllChats.value) { singleRecentChat ->
                    SingleRecentChat(singleRecentChat , onClick = {
                        startChat(singleRecentChat)
                    })
                }
                item {
                    if(userAllChats.value.isEmpty()) {
                        Box( contentAlignment = Alignment.Center) {
                            LoadAnimation(
                                modifier = Modifier.size(200.dp),
                                animation = R.raw.noresult,
                                playAnimation = true
                            )
                        }
                    }
                }
            }
        }
        else {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(BackGroundColor), contentAlignment = Alignment.Center
            ) {
                LoadAnimation(
                    modifier = Modifier.size(200.dp),
                    animation = R.raw.nonetwork,
                    playAnimation = true
                )
            }
        }

    }
}