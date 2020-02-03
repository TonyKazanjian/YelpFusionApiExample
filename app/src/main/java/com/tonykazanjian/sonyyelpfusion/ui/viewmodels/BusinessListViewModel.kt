package com.tonykazanjian.sonyyelpfusion.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonykazanjian.sonyyelpfusion.data.Business
import com.tonykazanjian.sonyyelpfusion.data.YelpInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BusinessListViewModel @Inject constructor(private val yelpInteractor: YelpInteractor): ViewModel() {

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
            viewModelScope.launch {
                try{
                    yelpInteractor.getBusinesses(termToSearch, latitude, longitude, offset = offset).apply {
                        listLiveData.postValue(this)
                        isLoading.value = false
                    }
                } catch (error: Throwable){
                    isLoading.value = false
                }

            }
//            disposable = yelpInteractor.getBusinesses(termToSearch, latitude, longitude, offset = offset)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doFinally { isLoading.value = false }
//                .subscribe ({list -> listLiveData.postValue(list)}, this::onError)
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
//        disposable?.let {
//            if (!it.isDisposed){
//                it.dispose()
//            }
//        }
    }

    private fun onError(e: Throwable){
        isError.value = true
        e.printStackTrace()
    }
}