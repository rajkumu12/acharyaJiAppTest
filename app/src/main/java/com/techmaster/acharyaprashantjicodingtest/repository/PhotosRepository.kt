package com.techmaster.acharyaprashantjicodingtest.repository

import Results
import Root
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.techmaster.acharyaprashantjicodingtest.api.ApiService
import com.techmaster.acharyaprashantjicodingtest.utility.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


class PhotosRepository @Inject constructor(private val apiService: ApiService) {
    private val _photosResponseData = MutableLiveData<NetworkResult<Root>>()
    val photosResponseLiveData: LiveData<NetworkResult<Root>>
        get() = _photosResponseData

    suspend fun getPhotos() {
        try {
            _photosResponseData.postValue(NetworkResult.Loading())
            val response = apiService.getPhotos()
            handleResponse(response, 1)
        } catch (e: Exception) {
            _photosResponseData.postValue(NetworkResult.Error("Something Went Wrong"))
        }

    }

    private fun handleResponse(response: Response<Root>, type: Int) {
        if (response.isSuccessful && response.body() != null) {
            _photosResponseData.postValue(NetworkResult.Success(response.body()!!, type))
        } else if (response.errorBody() != null) {
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _photosResponseData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } catch (e: Exception) {
                _photosResponseData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } else {
            _photosResponseData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
}
