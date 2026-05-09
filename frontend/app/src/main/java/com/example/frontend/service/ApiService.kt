package com.example.frontend.service

import com.example.frontend.model.ApiResponse
import retrofit2.http.GET

interface ApiService {

    @GET("api/get_metadata_api")
    suspend fun getMetaData(): ApiResponse

}