package com.tonykazanjian.sonyyelpfusion.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonykazanjian.sonyyelpfusion.data.YelpInteractor
import com.tonykazanjian.sonyyelpfusion.domain.Business
import com.tonykazanjian.sonyyelpfusion.domain.Review
import kotlinx.coroutines.launch
import javax.inject.Inject

class BusinessDetailViewModel @Inject constructor(private val yelpInteractor: YelpInteractor): ViewModel(){

    private val businessLiveData = MutableLiveData<Business>()

    private val reviewsLiveData = MutableLiveData<List<Review>>()

    private val photosLiveData = MutableLiveData<List<String>>()

    private var business: Business? = null

    private val isError = MutableLiveData<Boolean>().also {
        it.value = false
    }

    private val isLoading = MutableLiveData<Boolean>().also {
        it.value = false
    }

    fun fetchBusinessDetail(alias: String?){
        isLoading.value = true
        alias?.let{
            viewModelScope.launch {
                yelpInteractor.getBusinessByAlias(alias).apply {
                    setBusinessData(this)
                    yelpInteractor.getBusinessReviews(this.alias).apply {
                        reviewsLiveData.postValue(this)
                    }
                }
            }
        }
    }

    private fun setBusinessData(business: Business) {
        this.business = business
        photosLiveData.postValue(business.photos)
        businessLiveData.postValue(business)
    }

    fun getTitle(): String? {
        return business?.name
    }

    fun getPrice(): String? {
        return business?.price
    }

    fun getRating(): Float? {
        return business?.rating
    }

    fun getPhoneNumber(): String? {
        return business?.phoneNumber
    }

    fun getAddress(): String?{
        val addressBuilder = StringBuilder()
        business?.location?.address?.let {
            addressList ->
            for (index in addressList.indices){
                addressBuilder.append(addressList[index] + "\n")
            }
        }
        return addressBuilder.toString()
    }

    fun getBusinessLiveData(): LiveData<Business>{
        return businessLiveData
    }

    fun getReviewsLiveData(): LiveData<List<Review>>{
        return reviewsLiveData
    }

    fun getPhotosLiveData(): LiveData<List<String>> {
        return photosLiveData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun isError(): LiveData<Boolean> {
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