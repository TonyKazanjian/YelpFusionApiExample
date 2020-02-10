package com.tonykazanjian.sonyyelpfusion.domain

import com.tonykazanjian.sonyyelpfusion.data.YelpRepository
import javax.inject.Inject

class ReviewUseCase @Inject constructor(val repository: YelpRepository): UseCase<List<Review>, ReviewUseCase.Params>() {

    data class Params(val alias: String)

    override suspend fun run(params: Params): Result<List<Review>> {
        return try{
            val result = repository.getBusinessReviews(params.alias).map {
                it.toReview()
            }
            Result.success(result)
        } catch (t: Throwable){
            Result.failure(t)
        }
    }
}