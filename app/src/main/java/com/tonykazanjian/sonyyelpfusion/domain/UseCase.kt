package com.tonykazanjian.sonyyelpfusion.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

abstract class UseCase <out Type, in Params> where Type: Any {

    abstract suspend fun run(params: Params): Result<Type>

    suspend operator fun invoke(params: Params, onSuccess: (Type) -> Unit, onFailure: (Throwable) -> Unit){
        val result = run(params)
        coroutineScope {
            launch(Dispatchers.Main){
                result.fold(
                    onSuccess = onSuccess,
                    onFailure =  onFailure
                )
            }
        }
    }
}