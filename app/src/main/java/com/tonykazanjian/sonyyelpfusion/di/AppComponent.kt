package com.tonykazanjian.sonyyelpfusion.di

import com.tonykazanjian.sonyyelpfusion.YelpApplication
import com.tonykazanjian.sonyyelpfusion.ui.BusinessDetailActivity
import com.tonykazanjian.sonyyelpfusion.ui.BusinessListActivity
import dagger.Component
import javax.inject.Singleton

/**
 * @author Tony Kazanjian
 */
@Singleton
@Component(modules = [AppModule::class, RetrofitModule::class, RestApiModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(application: YelpApplication)
    fun inject(target: BusinessListActivity)
    fun inject(target: BusinessDetailActivity)
}