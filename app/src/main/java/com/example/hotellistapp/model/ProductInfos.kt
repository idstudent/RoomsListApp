package com.example.hotellistapp.model

import java.io.Serializable

data class ProductInfos(
    var id : Int,
    var title : String,
    var thumbnail : String,
    var imgUrl : String,
    var subject : String,
    var price : Int,
    var rate : Double,
    var time : String = "",
    var check: Boolean = false
): Serializable