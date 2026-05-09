package com.example.frontend.model

data class RecommendationRequest(
    val student_name: String,
    val difficulty: String,
    val interests: List<String>,
    val finished_courses: List<String>
)
