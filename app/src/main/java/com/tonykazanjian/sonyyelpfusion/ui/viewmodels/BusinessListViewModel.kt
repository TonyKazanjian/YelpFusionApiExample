package com.tonykazanjian.sonyyelpfusion.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tonykazanjian.sonyyelpfusion.data.Business
import com.tonykazanjian.sonyyelpfusion.data.YelpInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BusinessListViewModel @Inject constructor(private val yelpInteractor: YelpInteractor): ViewModel() {

    private var disposable: Disposable? = null

    private val listLiveData = MutableLiveData<List<Business>>()

    private val isError = MutableLiveData<Boolean>().also {
        it.value = false
    }

    private val isLoading = MutableLiveData<Boolean>().also {
        it.value = false
    }

    var searchTerm: String? = ""
    var latitude: String = ""
    var longitude: String = ""

    fun fetchBusinesses(searchTerm: String? = "", offset: Int = 0){
        val termToSearch = if (searchTerm.isNullOrEmpty()){
            this.searchTerm
        } else {
            searchTerm
        }

        isLoading.value = true
        termToSearch?.let{
            disposable = yelpInteractor.getBusinesses(termToSearch, latitude, longitude, offset = offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { isLoading.value = false }
                .subscribe ({list -> listLiveData.postValue(list)}, this::onError)
        }
    }

    fun getBusinesses(): LiveData<List<Business>>{
        return listLiveData
    }

    fun isLoading(): LiveData<Boolean>{
        return isLoading
    }

    fun isError(): LiveData<Boolean>{
        return isError
    }

    fun clearDisposable(){
        disposable?.let {
            if (!it.isDisposed){
                it.dispose()
            }
        }
    }

    private fun onError(e: Throwable){
        isError.value = true
        e.printStackTrace()
    }
}