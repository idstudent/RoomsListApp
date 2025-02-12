package com.example.hotellistapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hotellistapp.db.entity.RoomsRememberEntity
import io.reactivex.Maybe

@Dao
abstract class RoomsRememberDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(obj: RoomsRememberEntity): Long

    @Query("select * from rooms_remember")
    abstract fun select() : Maybe<List<RoomsRememberEntity>>

    @Query("select * from rooms_remember order by time desc")
    abstract fun latelyAddSelect() : Maybe<List<RoomsRememberEntity>>

    @Query("select * from rooms_remember order by rate asc")
    abstract fun rateASC() : Maybe<List<RoomsRememberEntity>>

    @Query("select * from rooms_remember order by rate desc")
    abstract fun rateDESC() : Maybe<List<RoomsRememberEntity>>

    @Query("delete from rooms_remember where id in (:id)")
    abstract fun deleteItem(id : Int)
}