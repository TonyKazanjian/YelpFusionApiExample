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
    private val businessListObserver: Observer<List<Business>> = mock()
    private val errorObserver: Observer<Boolean> = mock()

    private val business = Business("test-business", "Test Business", "", false, "", "$$", 5f,  listOf(), Location(
        listOf()))
    private val businessList = mutableListOf<Business>().apply { add(business) }

    @Before
    fun setUp() {
        viewModel = BusinessListViewModel(YelpInteractor(mockApiService))
        viewModel.getBusinesses().observeForever(businessListObserver)
        viewModel.isLoading().observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        viewModel.clearDisposable()
    }

    @Test
    fun testBusinessFetch() {
        givenApiReturnsData(BusinessesResponse(businessList))
        whenDataIsFetched()
        thenBusinessLiveDataIsUpdated()
    }

    @Test
    fun testErrorDisplayed(){
        givenApiReturnsError()
        whenDataIsFetched()
        thenErrorLiveDataIsUpdated()
    }

    private fun givenApiReturnsData(response: BusinessesResponse){
        `when`(mockApiService.getBusinesses(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyInt())).thenReturn(Observable.just(response))
    }

    private fun whenDataIsFetched(){
        viewModel.fetchBusinesses("test")
    }

    private fun thenBusinessLiveDataIsUpdated(){
        assertEquals(businessList, viewModel.getBusinesses().value)
    }

    private fun givenApiReturnsError(){
        `when`(mockApiService.getBusinesses(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyInt())).thenReturn(Observable.error(Exception("Test Error")))
    }

    private fun thenErrorLiveDataIsUpdated(){
        assertEquals(true, viewModel.isError().value)

    }

}

