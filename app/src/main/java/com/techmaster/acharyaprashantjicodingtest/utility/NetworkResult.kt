package com.techmaster.acharyaprashantjicodingtest.utility

sealed class NetworkResult<T>(val data: T? = null, val message: String? = null,val type: Int? = null) {

    class Success<T>(data: T,type:Int?) : NetworkResult<T>(data, type = type)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()

}