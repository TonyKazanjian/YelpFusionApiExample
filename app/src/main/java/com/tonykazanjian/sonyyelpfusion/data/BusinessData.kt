package com.tonykazanjian.sonyyelpfusion.data

import com.google.gson.annotations.SerializedName

data class Business(
    @SerializedName("alias") val alias: String,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("url") val businessUrl: String,
    @SerializedName("price") val price: String,
    @SerializedName("rating") val rating: Float,
    @SerializedName("photos") val photos: List<String>,
    @SerializedName("location") val location: Location
)

data class Location(
    @SerializedName("display_address") val address: List<String>
)

data class BusinessesResponse(
    @SerializedName("businesses") val businessList: List<Business>
)