package com.example.campus_teamup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBarState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.campus_teamup.R
import com.example.campus_teamup.mysealedClass.BottomNavScreens
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen() {
    val navController = rememberNavController()
    val bgColor = if(isSystemInDarkTheme()) Black else White
    val textColor = if (isSystemInDarkTheme()) White else Black
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = topAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                ),
                navigationIcon = {
                    IconButton(onClick = {

                    }) {
                        Icon(painter = painterResource(id = R.drawable.browseback), contentDescription =null , tint = textColor )
                    }
                }
            )
        }
     , content= {paddingValues ->
         LazyColumn(
             modifier = Modifier
                 .padding(paddingValues)
                 .fillMaxSize()
                 .background(bgColor)
                 , 
             verticalArrangement = Arrangement.spacedBy(8.dp),
             horizontalAlignment = Alignment.CenterHorizontally,
         ){
             items(10){
                 SingleNotification()
             }
         }
        })
}