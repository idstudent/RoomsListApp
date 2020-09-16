package com.example.hotellistapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hotellistapp.db.dao.RoomsRememberDAO
import com.example.hotellistapp.db.entity.RoomsRememberEntity

@Database(entities = [
    RoomsRememberEntity::class
],version = 1, exportSchema = false)

abstract class DBManager : RoomDatabase(){
    abstract fun roomsRememberDAO() : RoomsRememberDAO

    companion object {
        private var INSTANCE: DBManager? = null

        fun getInstance(context: Context): DBManager? {
            if (INSTANCE == null) {
                synchronized(DBManager::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, DBManager::class.java, "rooms_list.db").build()
                }
            }
            return INSTANCE
        }
    }
}