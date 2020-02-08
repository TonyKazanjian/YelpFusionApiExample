package com.tonykazanjian.sonyyelpfusion.data

import com.tonykazanjian.sonyyelpfusion.ApiUtils
import com.tonykazanjian.sonyyelpfusion.entity.BusinessEntity
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface YelpApiService {

    @GET("businesses/search")
    suspend fun getBusinesses(@Header(ApiUtils.AUTHORIZATION_HEADER) authToken: String,
                      @Query("term") searchTerm: String,
                      @Query("latitude") latitude: String,
                      @Query("longitude") longitude: String,
                      @Query("offset") offset: Int): BusinessesResponse

    @GET("businesses/{id}")
    suspend fun getBusinessById(@Header(ApiUtils.AUTHORIZATION_HEADER) authToken: String, @Path("id") alias: String): BusinessEntity

    @GET("businesses/{id}/reviews")
    suspend fun getBusinessReviews(@Header(ApiUtils.AUTHORIZATION_HEADER) authToken: String, @Path("id") alias: String): ReviewResponse}