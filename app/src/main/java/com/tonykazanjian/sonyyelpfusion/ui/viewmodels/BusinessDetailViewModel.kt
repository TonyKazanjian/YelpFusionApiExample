package com.tonykazanjian.sonyyelpfusion.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tonykazanjian.sonyyelpfusion.data.Business
import com.tonykazanjian.sonyyelpfusion.data.Review
import com.tonykazanjian.sonyyelpfusion.data.YelpInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BusinessDetailViewModel @Inject constructor(private val yelpInteractor: YelpInteractor): ViewModel(){
    private var disposable: Disposable? = null

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
            disposable = yelpInteractor.getBusinessByAlias(alias)
                .subscribeOn(Schedulers.io())
                .doOnNext (this::setBusinessData)
                .flatMap { business -> yelpInteractor.getBusinessReviews(business.alias) }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { isLoading.value = false }
                .subscribe ({reviews -> reviewsLiveData.postValue(reviews)
                }, this::onError)
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

    private fun onError(e: Throwable){
        isError.value = true
        e.printStackTrace()
    }


}