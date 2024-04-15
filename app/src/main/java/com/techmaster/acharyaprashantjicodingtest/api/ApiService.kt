package com.techmaster.acharyaprashantjicodingtest.api

import Root
import com.techmaster.acharyaprashantjicodingtest.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/photos")
    suspend fun getPhotos(
        @Query("page") currentPage: Int,
        @Query("query") query: String = "office",
        @Query("client_id") clientId: String = BuildConfig.secretkey
    ): Response<Root>
}