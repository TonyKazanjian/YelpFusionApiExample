package com.tonykazanjian.sonyyelpfusion.data

import com.google.gson.annotations.SerializedName

data class Business(
    @SerializedName("alias") val alias: String,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("is_closed") val isClosed: Boolean,
    @SerializedName("url") val businessUrl: String,
    @SerializedName("price") val price: String? = "",
    @SerializedName("rating") val rating: Float,
    @SerializedName("photos") val photos: List<String>,
    @SerializedName("location") val location: Location,
    @SerializedName("display_phone") val phoneNumber: String? = ""
)

data class Location(
    @SerializedName("display_address") val address: List<String>
)

data class BusinessesResponse(
    @SerializedName("businesses") val businessList: List<Business>
)

data class Review(
    @SerializedName("url") val reviewUrl: String,
    @SerializedName("text") val reviewText: String,
    @SerializedName("rating") val rating: Float,
    @SerializedName("time_created") val reviewDate: String,
    @SerializedName("user") val user: User
)

data class User(
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("name") val name: String
)

data class ReviewResponse(
    @SerializedName("reviews") val reviewsList: List<Review>
)

