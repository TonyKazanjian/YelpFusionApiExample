package com.tonykazanjian.sonyyelpfusion.di

import com.tonykazanjian.sonyyelpfusion.ApiUtils
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Singleton
    @Provides
    @Named(value = "retrofit")
    fun providesRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiUtils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}