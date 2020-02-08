package com.tonykazanjian.sonyyelpfusion.domain

data class Business(
    val alias: String,
    val name: String,
    val imageUrl: String,
    val isClosed: Boolean,
    val businessUrl: String,
    val price: String,
    val rating: Float,
    val photos: List<String>,
    val location: Location,
    val phoneNumber: String
)