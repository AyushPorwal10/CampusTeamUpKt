package com.example.new_campus_teamup.screens

import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.ui.theme.White
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