package com.example.hotellistapp.model

import com.example.hotellistapp.api.RoomsResponse
import java.io.Serializable

data class ProductInfos(
    var id : Int,
    var name : String,
    var thumbnail : String,
    var imgUrl : String,
    var subject : String,
    var price : Int,
    var rate : Double,
    var check: Boolean = false
): Serializable