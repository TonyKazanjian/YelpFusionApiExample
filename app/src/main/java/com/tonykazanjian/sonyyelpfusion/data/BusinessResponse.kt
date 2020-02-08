package com.tonykazanjian.sonyyelpfusion.data

import com.google.gson.annotations.SerializedName
import com.tonykazanjian.sonyyelpfusion.entity.BusinessEntity

data class BusinessesResponse(
    @SerializedName("businesses") val businessList: List<BusinessEntity>
)