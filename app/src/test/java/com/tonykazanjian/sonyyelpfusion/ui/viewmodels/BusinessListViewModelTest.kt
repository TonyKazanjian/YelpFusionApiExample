package com.tonykazanjian.sonyyelpfusion.ui.viewmodels

import androidx.lifecycle.Observer
import com.tonykazanjian.sonyyelpfusion.data.*
import io.reactivex.Observable
import org.junit.*

import org.junit.Assert.*
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

class BusinessListViewModelTest: BaseViewModelTest() {

    private lateinit var viewModel: BusinessListViewModel
    private val mockApiService: YelpApiService = mock()
    private val observer: Observer<List<Business>> = mock()

    private val business = Business("test-business", "Test Business", "", "", "$$", 5.0f, listOf(), Location(
        listOf()))
    private val businessList = mutableListOf<Business>().apply { add(business) }

    @Before
    fun setUp() {
        viewModel = BusinessListViewModel(YelpInteractor(mockApiService))
        viewModel.getBusinesses().observeForever(observer)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testBusinessFetch() {
        givenApiReturnsData(BusinessesResponse(businessList))
        whenDataIsFetched()
        thenLiveDataIsUpdated()
    }


    @Test
    fun testLoadingDisplayed() {

    }

    @Test
    fun testErrorDisplayed(){

    }

    private fun givenApiReturnsData(response: BusinessesResponse){
        `when`(mockApiService.getBusinesses(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString())).thenReturn(Observable.just(response))
    }

    private fun whenDataIsFetched(){
        viewModel.fetchBusinesses("test")
    }

    private fun thenLiveDataIsUpdated(){
        assertEquals(businessList, viewModel.getBusinesses().value)
    }
}

