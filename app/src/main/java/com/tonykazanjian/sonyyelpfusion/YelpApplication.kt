package com.tonykazanjian.sonyyelpfusion

import android.app.Application
import com.tonykazanjian.sonyyelpfusion.di.AppComponent
import com.tonykazanjian.sonyyelpfusion.di.AppModule
import com.tonykazanjian.sonyyelpfusion.di.DaggerAppComponent

class YelpApplication: Application() {

    val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        //TODO - deprecation
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}