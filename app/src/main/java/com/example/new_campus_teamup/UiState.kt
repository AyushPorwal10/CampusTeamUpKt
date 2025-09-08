package com.example.new_campus_teamup

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()

    object Idle : UiState<Nothing>()
    data class Success<T>(val data : T) : UiState<T>()
    data class Error(val errorMessage : String) : UiState<Nothing>()
}