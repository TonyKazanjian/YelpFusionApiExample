package com.tonykazanjian.sonyyelpfusion.data

import com.tonykazanjian.sonyyelpfusion.ApiUtils
import com.tonykazanjian.sonyyelpfusion.domain.Business
import com.tonykazanjian.sonyyelpfusion.domain.Review
import javax.inject.Inject

class YelpInteractor @Inject constructor(private var yelpApiService: YelpApiService){

    suspend fun getBusinesses(searchTerm: String, latitude: String, longitude: String, offset: Int = 0): List<Business>{
        return yelpApiService.getBusinesses(ApiUtils.createHeader(), searchTerm, latitude, longitude, offset).businessList.map {
            it.toBusiness()
        }
    }

    suspend fun getBusinessByAlias(alias: String): Business{
        return yelpApiService.getBusinessById(ApiUtils.createHeader(), alias).toBusiness()
    }

    suspend fun getBusinessReviews(alias: String): List<Review>{
        return yelpApiService.getBusinessReviews(ApiUtils.createHeader(), alias).reviewsList.map {
            it.toReview()
        }
    }
}