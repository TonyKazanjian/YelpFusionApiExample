package com.tonykazanjian.sonyyelpfusion.entity

import com.google.gson.annotations.SerializedName
import com.tonykazanjian.sonyyelpfusion.domain.User

data class UserEntity(
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("name") val name: String
){
    fun toUser() = User(imageUrl, name)
}