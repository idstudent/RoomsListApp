package com.example.hotellistapp.model.api

import com.google.gson.JsonElement
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("json/{num}")
    suspend fun getRoomsList(
        @Path("num") num : String
    ) : JsonElement

    companion object {
        const val  BASE_URL = "https://www.gccompany.co.kr/App/"
    }
}