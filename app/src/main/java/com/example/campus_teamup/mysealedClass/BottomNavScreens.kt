package com.example.campus_teamup.mysealedClass

sealed class BottomNavScreens(val screen : String) {
    data object Roles : BottomNavScreens("roles")
    data object Vacancies : BottomNavScreens("vacancies")
    data object Projects : BottomNavScreens("projects")
}