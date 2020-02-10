package com.tonykazanjian.sonyyelpfusion.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonykazanjian.sonyyelpfusion.data.YelpRepository
import com.tonykazanjian.sonyyelpfusion.domain.Business
import com.tonykazanjian.sonyyelpfusion.domain.BusinessListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class BusinessListViewModel @Inject constructor(private val listUseCase: BusinessListUseCase): ViewModel() {

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
        termToSearch?.let{term ->
            viewModelScope.launch {
                listUseCase(BusinessListUseCase.Params(term, latitude, longitude, offset), {
                    listLiveData.postValue(it)
                    isLoading.value = false
                }, {onError(it)})
            }
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
        isLoading.value = false
        isError.value = true
        e.printStackTrace()
    }
}