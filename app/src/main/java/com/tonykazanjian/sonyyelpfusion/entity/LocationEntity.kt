package com.tonykazanjian.sonyyelpfusion.entity

import com.google.gson.annotations.SerializedName
import com.tonykazanjian.sonyyelpfusion.domain.Location

data class LocationEntity(
    @SerializedName("display_address") val address: List<String>
) {
    fun toLocation() = Location(address)
}