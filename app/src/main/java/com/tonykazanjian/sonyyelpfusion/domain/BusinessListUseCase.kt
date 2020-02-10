package com.tonykazanjian.sonyyelpfusion.domain

import com.tonykazanjian.sonyyelpfusion.data.YelpRepository
import javax.inject.Inject

class BusinessListUseCase @Inject constructor(private val repository: YelpRepository): UseCase<List<Business>, BusinessListUseCase.Params>(){

    override suspend fun run(params: Params): Result<List<Business>> {
        return try {
            val businesses = repository.getBusinesses(params.searchTerm, params.latitude, params.longitude, params.offset).map {
                it.toBusiness()
            }
            Result.success(businesses)
        } catch (t: Throwable){
            Result.failure(t)
        }
    }

    data class Params(val searchTerm: String, val latitude: String, val longitude: String, val offset: Int)

}