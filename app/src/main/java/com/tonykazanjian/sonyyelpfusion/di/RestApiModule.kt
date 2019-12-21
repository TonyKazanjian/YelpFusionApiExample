package com.tonykazanjian.sonyyelpfusion.di

import com.tonykazanjian.sonyyelpfusion.data.YelpApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class RestApiModule {

    @Provides
    @Singleton
    fun providesYelpApiService(@Named("retrofit")yelpRetrofit: Retrofit): YelpApiService = yelpRetrofit.create(YelpApiService::class.java)
}