package com.example.hotellistapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager {
    private lateinit var instance : ApiService

    fun getInstance() : ApiService {
        if(!::instance.isInitialized) {
            instance = Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
        return instance
    }
}