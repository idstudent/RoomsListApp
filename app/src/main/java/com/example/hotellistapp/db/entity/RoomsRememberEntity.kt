package com.example.hotellistapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms_remember")
data class RoomsRememberEntity (
    @PrimaryKey
    val id : Int,
    val name : String,
    val thumbnail : String,
    val imgPath : String,
    val subject : String,
    val price : Int,
    val rate : Double,
    val time : String,
    var check: Boolean = false
)