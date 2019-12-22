package com.tonykazanjian.sonyyelpfusion.data

import com.tonykazanjian.sonyyelpfusion.ApiUtils
import io.reactivex.Observable
import javax.inject.Inject

class YelpInteractor @Inject constructor(private var yelpApiService: YelpApiService){

    fun getBusinesses(searchTerm: String, latitude: String = "37.78688", longitude: String = "-122.399972"): Observable<List<Business>>{
        return yelpApiService.getBusinesses(ApiUtils.createHeader(), searchTerm, latitude, longitude).map {
            it.businessList
        }
    }

    fun getBusinessById(alias: String): Observable<Business>{
        return yelpApiService.getBusinessById(ApiUtils.createHeader(), alias)
    }
}