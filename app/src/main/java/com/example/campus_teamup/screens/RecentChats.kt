package com.example.campus_teamup.screens

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.Dimensions
import com.example.campus_teamup.helper.LoadAnimation
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.rememberNetworkStatus
import com.example.campus_teamup.mydataclass.RecentChats
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightTextColor
import com.example.campus_teamup.ui.theme.LightWhite
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.RecentChatsViewModel


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