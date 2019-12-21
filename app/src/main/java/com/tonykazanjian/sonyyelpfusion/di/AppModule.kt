package com.tonykazanjian.sonyyelpfusion.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Tony Kazanjian
 */
@Module
class AppModule(private val app: Application){
    @Provides
    @Singleton
    fun provideContext(): Context = app
}