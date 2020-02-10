package com.tonykazanjian.sonyyelpfusion.data

import com.tonykazanjian.sonyyelpfusion.ApiUtils
import com.tonykazanjian.sonyyelpfusion.domain.Business
import com.tonykazanjian.sonyyelpfusion.domain.Review
import com.tonykazanjian.sonyyelpfusion.entity.BusinessEntity
import com.tonykazanjian.sonyyelpfusion.entity.ReviewEntity
import javax.inject.Inject

class YelpRepository @Inject constructor(private var yelpApiService: YelpApiService){

    suspend fun getBusinesses(searchTerm: String, latitude: String, longitude: String, offset: Int = 0): List<BusinessEntity>{
        return yelpApiService.getBusinesses(ApiUtils.createHeader(), searchTerm, latitude, longitude, offset).businessList
    }

    suspend fun getBusinessByAlias(alias: String): BusinessEntity{
        return yelpApiService.getBusinessById(ApiUtils.createHeader(), alias)
    }

    suspend fun getBusinessReviews(alias: String): List<ReviewEntity>{
        return yelpApiService.getBusinessReviews(ApiUtils.createHeader(), alias).reviewsList
    }
}