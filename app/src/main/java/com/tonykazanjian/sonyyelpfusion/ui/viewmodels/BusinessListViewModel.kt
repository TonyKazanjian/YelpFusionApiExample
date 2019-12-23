package com.tonykazanjian.sonyyelpfusion.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.tonykazanjian.sonyyelpfusion.data.YelpApiService
import com.tonykazanjian.sonyyelpfusion.data.YelpInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BusinessListViewModel @Inject constructor(yelpInteractor: YelpInteractor): ViewModel() {

    private var disposable: Disposable? = null
    init {
        disposable = yelpInteractor.getBusinesses("pizza")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                Log.d("TONY", "${it.size}")
            }, this::onError)
    }

    private fun onError(e: Throwable){
        e.printStackTrace()
    }
}