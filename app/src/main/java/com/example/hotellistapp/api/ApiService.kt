package com.example.hotellistapp.api

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("json/{num}")
    fun getRoomsList(
        @Path("num") num : String
    ) : Single<RoomsResponse>
    @GET("json/{num}")
    fun getRoomsList1(
        @Path("num") num : String
    ) : Single<RoomsResponse>

    companion object {
        const val  BASE_URL = "https://www.gccompany.co.kr/App/"
    }
}