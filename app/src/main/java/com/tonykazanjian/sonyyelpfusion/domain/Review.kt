package com.tonykazanjian.sonyyelpfusion.domain

data class Review(
    val reviewUrl: String,
    val reviewText: String,
    val rating: Float,
    val reviewDate: String,
    val user: User
)