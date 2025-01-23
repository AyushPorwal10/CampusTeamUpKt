package com.example.campus_teamup.mysealedClass

sealed class DrawerItemScreens (val screen : String){
    data object Notifications : DrawerItemScreens("notifications")
    data object TeamDetails : DrawerItemScreens("teamdetails")

    data object Chats : DrawerItemScreens("chats")
}


