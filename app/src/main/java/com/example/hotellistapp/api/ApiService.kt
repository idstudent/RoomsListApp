package com.example.hotellistapp.api

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("json/{num}")
    suspend fun getRoomsList(
        @Path("num") num : String
    ) : RoomsResponse

    companion object {
        const val  BASE_URL = "https://www.gccompany.co.kr/App/"
    }
}