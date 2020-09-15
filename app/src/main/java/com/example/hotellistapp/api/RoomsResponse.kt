package com.example.hotellistapp.api

import com.google.gson.annotations.SerializedName

data class RoomsResponse(
    @SerializedName("msg")
    val msg : String,
    @SerializedName("data")
    val data : Data,
    @SerializedName("code")
    val code : Int
){
    data class Data(
        @SerializedName("totalCount")
        val cnt : Int,
        @SerializedName("product")
        val product : List<Product>
    ){
        data class Product(
            @SerializedName("id")
            val id : Int,
            @SerializedName("name")
            val name : String,
            @SerializedName("thumbnail")
            val image : String,
            @SerializedName("description")
            val info : Description,
            @SerializedName("rate")
            val rate : Double
        ){
            data class Description(
                @SerializedName("imagePath")
                val imgPath : String,
                @SerializedName("subject")
                val subject : String,
                @SerializedName("price")
                val price : String
            )
        }
    }
}