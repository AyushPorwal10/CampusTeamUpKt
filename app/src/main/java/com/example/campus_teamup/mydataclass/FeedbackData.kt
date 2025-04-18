package com.example.campus_teamup.mydataclass

data class FeedbackData(
    val rating: Int = 0,
    val feedbackText: String = "",
    val wouldRecommend: Boolean? = null
)
