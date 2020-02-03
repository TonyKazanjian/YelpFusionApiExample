package com.tonykazanjian.sonyyelpfusion.data

import com.tonykazanjian.sonyyelpfusion.ApiUtils
import io.reactivex.Observable
import javax.inject.Inject

class YelpInteractor @Inject constructor(private var yelpApiService: YelpApiService){

    suspend fun getBusinesses(searchTerm: String, latitude: String, longitude: String, offset: Int = 0): List<Business>{
        return yelpApiService.getBusinesses(ApiUtils.createHeader(), searchTerm, latitude, longitude, offset).businessList
    }

    fun getBusinessByAlias(alias: String): Observable<Business>{
        return yelpApiService.getBusinessById(ApiUtils.createHeader(), alias)
    }

    fun getBusinessReviews(alias: String): Observable<List<Review>>{
        return yelpApiService.getBusinessReviews(ApiUtils.createHeader(), alias).map {
            response -> response.reviewsList
        }
    }
}