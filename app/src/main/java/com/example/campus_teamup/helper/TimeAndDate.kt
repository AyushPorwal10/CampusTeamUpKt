package com.example.campus_teamup.helper

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object TimeAndDate {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.systemDefault())
        return formatter.format(Instant.now())
    }


    // 12 march 20xx format
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(timestamp: Long): String {

        val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale.ENGLISH)
        return dateTime.format(formatter)
    }
}