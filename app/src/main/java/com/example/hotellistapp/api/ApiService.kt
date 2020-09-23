package com.example.hotellistapp.api

import com.google.gson.JsonElement
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("json/{num}")
    fun getRoomsList(
        @Path("num") num : String
    ) : Call<JsonElement>

    companion object {
        const val  BASE_URL = "https://www.gccompany.co.kr/App/"
    }
}