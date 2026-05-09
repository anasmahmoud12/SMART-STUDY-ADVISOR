package com.example.frontend.model

data class RecommendationResponse(
    val status: String,
    val message: String,
    val data: RecommendationData
)
