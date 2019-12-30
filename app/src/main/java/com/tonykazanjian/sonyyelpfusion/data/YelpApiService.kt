package com.tonykazanjian.sonyyelpfusion.data

import com.tonykazanjian.sonyyelpfusion.ApiUtils
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface YelpApiService {

    @GET("businesses/search")
    //TODO - location
    fun getBusinesses(@Header(ApiUtils.AUTHORIZATION_HEADER) authToken: String,
                      @Query("term") searchTerm: String,
                      @Query("latitude") latitude: String,
                      @Query("longitude") longitude: String,
                      @Query("offset") offset: Int): Observable<BusinessesResponse>

    @GET("businesses/{id}")
    fun getBusinessById(@Header(ApiUtils.AUTHORIZATION_HEADER) authToken: String, @Path("id") alias: String): Observable<Business>

    @GET("businesses/{id}/reviews")
    fun getBusinessReviews(@Header(ApiUtils.AUTHORIZATION_HEADER) authToken: String, @Path("id") alias: String): Observable<ReviewResponse>
}