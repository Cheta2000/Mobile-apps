package com.example.arkanoidgame

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProgressDAO {
    @Query("SELECT * FROM Progress")
    fun getAll():List<Progress>

    @Insert
    fun insertAll(vararg:Progress)

    @Query("DELETE FROM Progress")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM Progress")
    fun countScore():Int
}