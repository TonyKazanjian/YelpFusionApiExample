package com.tonykazanjian.sonyyelpfusion.di

import com.tonykazanjian.sonyyelpfusion.ui.BusinessDetailFragment
import com.tonykazanjian.sonyyelpfusion.ui.BusinessListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class, RestApiModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(target: BusinessListActivity)
    fun inject(target: BusinessDetailFragment)
}