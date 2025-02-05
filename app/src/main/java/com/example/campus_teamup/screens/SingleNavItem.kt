package com.example.campus_teamup.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.R
import com.example.campus_teamup.myactivities.DrawerItemActivity
import com.example.campus_teamup.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavItem(
    coroutineScope : CoroutineScope ,
    drawerState : DrawerState ,
    icon : Int,
    label : String,
    navigate : () -> Unit) {
    NavigationDrawerItem(
        label = { Text(text = label, color = White) },
        selected = false,
        icon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(26.dp)
            )
        },
        onClick = {
            coroutineScope.launch {
                drawerState.close()
            }
         navigate()
        }
    )
}