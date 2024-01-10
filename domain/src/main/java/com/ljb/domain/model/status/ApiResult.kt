package com.ljb.domain.model.status

sealed class ApiResult<out T>{
    data object Loading: ApiResult<Nothing>()
    data class Success<out T>(val data: T): ApiResult<T>()
    data class ApiError<T>(val message: String, val code: Int) : ApiResult<T>()
    data class NetworkError<T>(val throwable: Throwable) : ApiResult<T>()
}
