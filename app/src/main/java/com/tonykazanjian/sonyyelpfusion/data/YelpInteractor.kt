package com.tonykazanjian.sonyyelpfusion.data

import com.tonykazanjian.sonyyelpfusion.ApiUtils
import javax.inject.Inject

class YelpInteractor @Inject constructor(private var yelpApiService: YelpApiService){

    suspend fun getBusinesses(searchTerm: String, latitude: String, longitude: String, offset: Int = 0): List<Business>{
        return yelpApiService.getBusinesses(ApiUtils.createHeader(), searchTerm, latitude, longitude, offset).businessList
    }

    suspend fun getBusinessByAlias(alias: String): Business{
        return yelpApiService.getBusinessById(ApiUtils.createHeader(), alias)
    }

    suspend fun getBusinessReviews(alias: String): List<Review>{
        return yelpApiService.getBusinessReviews(ApiUtils.createHeader(), alias).reviewsList
    }
}