package com.example.hotellistapp.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//object ApiManager {
//    private lateinit var instance : ApiService
//
//    fun getInstance() : ApiService {
//        if(!::instance.isInitialized) {
//            instance = Retrofit.Builder()
//                .baseUrl("https://www.gccompany.co.kr/App/json/")
//                .client(
//                    OkHttpClient.Builder()
//                        .addInterceptor(HttpLoggingInterceptor().apply {
//                            level = HttpLoggingInterceptor.Level.BODY
//                        })
//                        .build()
//                )
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(ApiService::class.java)
//        }
//        return instance
//    }
//}