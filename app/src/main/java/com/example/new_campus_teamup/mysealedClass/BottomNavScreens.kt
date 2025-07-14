package com.example.new_campus_teamup.mysealedClass

sealed class BottomNavScreens(val screen : String) {
    data object Roles : BottomNavScreens("roles")
    data object Vacancies : BottomNavScreens("vacancies")
    data object Projects : BottomNavScreens("projects")
    data object Home : BottomNavScreens("home")
}