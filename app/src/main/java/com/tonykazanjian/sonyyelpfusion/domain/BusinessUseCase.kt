package com.tonykazanjian.sonyyelpfusion.domain

import com.tonykazanjian.sonyyelpfusion.data.YelpRepository
import javax.inject.Inject

class BusinessUseCase @Inject constructor(val repository: YelpRepository): UseCase<Business, BusinessUseCase.Params>(){

    data class Params(val alias: String)

    override suspend fun run(params: Params): Result<Business> {
        return try {
            val businesses = repository.getBusinessByAlias(params.alias).toBusiness()
            Result.success(businesses)
        } catch (t: Throwable){
            Result.failure(t)
        }
    }
}