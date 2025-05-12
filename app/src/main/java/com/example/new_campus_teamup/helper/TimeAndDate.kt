package com.example.new_campus_teamup.helper

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
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
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss", Locale.ENGLISH)
        return dateTime.format(formatter)
    }



     fun getTimeAgoFromDate(dateStr: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC") // or your local time zone if needed

        val postedDate: Date = try {
            dateFormat.parse(dateStr)!!
        } catch (e: Exception) {
            return "Invalid date"
        }

        val now = Calendar.getInstance().timeInMillis
        val postedTimeMillis = postedDate.time
        val diff = now - postedTimeMillis

        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val months = days / 30
        val years = days / 365

        return when {
            days == 0L -> "today"
            days in 1..29 -> "$days day${if (days > 1) "s" else ""} ago"
            months in 1..11 -> "$months month${if (months > 1) "s" else ""} ago"
            years == 1L -> "1 year ago"
            years > 1L -> ">1 year ago"
            else -> "Unknown"
        }
    }

    fun parseToDate(dateString: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return try {
            format.parse(dateString) ?: Date(0)
        } catch (e: Exception) {
            Date(0)
        }
    }

}