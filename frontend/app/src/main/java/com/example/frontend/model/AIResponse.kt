package com.example.frontend.model

data class AIResponse(
    val status: String,
    val response: String,
    val history: List<Message>
)
