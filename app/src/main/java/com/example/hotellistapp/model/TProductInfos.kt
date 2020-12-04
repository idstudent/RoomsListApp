package com.example.hotellistapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TProductInfos(
    @SerializedName("id")
    var id : Int,
    @SerializedName("name")
    var name : String,
    @SerializedName("thumbnail")
    var thumbnail : String,
    @SerializedName("description")
    var descriptionList : TestThumb,
    @SerializedName("rate")
    var rate : Double,
    var time : String = "",
    var check: Boolean = false
):Serializable
data class TestThumb(
    @SerializedName("imagePath")
    var imagePath : String,
    @SerializedName("subject")
    var subject : String,
    @SerializedName("price")
    var price : Int
):Serializable

