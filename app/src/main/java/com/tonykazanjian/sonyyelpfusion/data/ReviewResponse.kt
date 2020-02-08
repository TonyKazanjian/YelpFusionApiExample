package com.tonykazanjian.sonyyelpfusion.data

import com.google.gson.annotations.SerializedName
import com.tonykazanjian.sonyyelpfusion.entity.ReviewEntity

data class ReviewResponse(
    @SerializedName("reviews") val reviewsList: List<ReviewEntity>
)