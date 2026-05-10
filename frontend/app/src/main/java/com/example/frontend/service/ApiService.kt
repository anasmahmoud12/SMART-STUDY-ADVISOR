package com.example.frontend.service

import com.example.frontend.model.AIRequest
import com.example.frontend.model.AIResponse
import com.example.frontend.model.ApiResponse
import com.example.frontend.model.RecommendationRequest
import com.example.frontend.model.RecommendationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @GET("api/get_metadata_api")
    suspend fun getMetaData(): ApiResponse

    @POST("api/recommend")
    @Headers("Content-Type: application/json")
    suspend fun getRecommendation(@Body request: RecommendationRequest): RecommendationResponse

    @POST("api/chat_with_ai_api/")
    @Headers("Content-Type: application/json")
    suspend fun chatAI(@Body request: AIRequest): AIResponse
}