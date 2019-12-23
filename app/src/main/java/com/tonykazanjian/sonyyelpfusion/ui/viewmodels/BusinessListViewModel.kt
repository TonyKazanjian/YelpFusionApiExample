package com.tonykazanjian.sonyyelpfusion.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tonykazanjian.sonyyelpfusion.data.Business
import com.tonykazanjian.sonyyelpfusion.data.YelpApiService
import com.tonykazanjian.sonyyelpfusion.data.YelpInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BusinessListViewModel @Inject constructor(val yelpInteractor: YelpInteractor): ViewModel() {

    private var disposable: Disposable? = null

    private val listLiveData = MutableLiveData<List<Business>>()

    private val isError = MutableLiveData<Boolean>().also {
        it.value = false
    }

    private val isLoading = MutableLiveData<Boolean>().also {
        it.value = false
    }

    fun fetchBusinesses(searchTerm: String){
        isLoading.value = true
        disposable = yelpInteractor.getBusinesses(searchTerm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { isLoading.value = false }
            .subscribe ({
                listLiveData.postValue(it)
            }, this::onError)
    }

    fun getBusinesses(): LiveData<List<Business>>{
        return listLiveData
    }

    fun isLoading(): LiveData<Boolean>{
        return isLoading
    }

    private fun onError(e: Throwable){
        e.printStackTrace()
    }
}