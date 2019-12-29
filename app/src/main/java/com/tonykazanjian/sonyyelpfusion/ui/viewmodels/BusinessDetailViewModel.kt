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

class BusinessDetailViewModel @Inject constructor(val yelpInteractor: YelpInteractor): ViewModel(){
    private var disposable: Disposable? = null

    private val businessLiveData = MutableLiveData<Business>()

    val reviewsLiveData = MutableLiveData<List<Review>>()

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
                .doOnNext { business ->
                    this.business = business
                    businessLiveData.postValue(business) }
                .flatMap { business -> yelpInteractor.getBusinessReviews(business.alias) }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { isLoading.value = false }
                .subscribe ({reviews -> reviewsLiveData.postValue(reviews)
                }, this::onError)
        }
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

    fun getUrl(): String? {
        return business?.businessUrl
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

    fun getPhotos(): List<String>?{
        addImageUrlToPhotos()
        return businessLiveData.value?.photos
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    private fun addImageUrlToPhotos(){
        businessLiveData.value?.let {
            if (it.imageUrl.isNotEmpty()){
                it.photos.toMutableList().add(0, it.imageUrl)
            }
        }
    }

    private fun onError(e: Throwable){
        isError.value = true
        e.printStackTrace()
    }


}