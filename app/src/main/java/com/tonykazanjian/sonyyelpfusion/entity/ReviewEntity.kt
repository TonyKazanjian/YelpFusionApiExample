package com.tonykazanjian.sonyyelpfusion.entity

import com.google.gson.annotations.SerializedName
import com.tonykazanjian.sonyyelpfusion.domain.Review
import com.tonykazanjian.sonyyelpfusion.domain.User

data class ReviewEntity(
    @SerializedName("url") val reviewUrl: String,
    @SerializedName("text") val reviewText: String,
    @SerializedName("rating") val rating: Float,
    @SerializedName("time_created") val reviewDate: String,
    @SerializedName("user") val user: UserEntity
){
    fun toReview() = Review(reviewUrl, reviewText, rating, reviewDate, user.toUser())
}