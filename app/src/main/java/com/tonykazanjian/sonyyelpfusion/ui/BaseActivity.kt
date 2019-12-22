package com.tonykazanjian.sonyyelpfusion.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.tonykazanjian.sonyyelpfusion.YelpApplication
import com.tonykazanjian.sonyyelpfusion.di.AppComponent
import com.tonykazanjian.sonyyelpfusion.di.ViewModelFactory
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {

    @Inject
    protected lateinit var viewModeFactory: ViewModelFactory

    protected val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (this.application as YelpApplication).appComponent
    }
}