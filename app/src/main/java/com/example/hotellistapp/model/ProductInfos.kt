package com.example.hotellistapp.model

import com.example.hotellistapp.api.RoomsResponse

data class ProductInfos(
    var name : String,
    var thumbnail : String,
    var imgUrl : String,
    var subject : String,
    var price : String,
    var rate : Double
)