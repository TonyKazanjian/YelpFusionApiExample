package com.tonykazanjian.sonyyelpfusion.entity

import com.google.gson.annotations.SerializedName
import com.tonykazanjian.sonyyelpfusion.domain.Business

data class BusinessEntity(
    @SerializedName("alias") val alias: String,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("is_closed") val isClosed: Boolean,
    @SerializedName("url") val businessUrl: String,
    @SerializedName("price") val price: String? = "",
    @SerializedName("rating") val rating: Float,
    @SerializedName("photos") val photos: List<String>? = listOf(),
    @SerializedName("location") val location: LocationEntity,
    @SerializedName("display_phone") val phoneNumber: String? = ""){

    fun toBusiness(): Business{
        val price = price ?: ""
        val photos = photos?: listOf()
        val phoneNumber = phoneNumber?: ""
        return Business(alias, name, imageUrl, isClosed, businessUrl, price, rating, photos, location.toLocation(), phoneNumber)
    }
}