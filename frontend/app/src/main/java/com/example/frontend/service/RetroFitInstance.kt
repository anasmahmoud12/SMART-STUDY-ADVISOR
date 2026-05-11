package com.example.frontend.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitInstance {

//    val BASE_URL: String = "http://192.168.1.3:8000/"
    val BASE_URL = "http://192.168.1.8:8000/"
    val api: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)

    }
}