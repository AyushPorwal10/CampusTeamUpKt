package com.example.new_campus_teamup.mydataclass

import androidx.annotation.Keep

@Keep
data class FeedbackData(
    val rating: Int = 0,
    val feedbackText: String = "",
    val wouldRecommend: Boolean? = null
)
