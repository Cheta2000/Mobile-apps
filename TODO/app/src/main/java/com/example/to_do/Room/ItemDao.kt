package com.example.to_do.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.to_do.Room.MyItem

@Dao
interface ItemDao {
    @Query("SELECT * FROM MyItem")
    fun getAll(): MutableList<MyItem>

    @Insert
    fun insertAll(vararg items: MyItem)

    @Delete
    fun delete(item: MyItem)

    @Query("DELETE FROM MyItem")
    fun deleteAll()

    @Query("DELETE FROM MyItem Where id=:id")
    fun deleteByID(id:Int)

    @Query("SELECT * FROM MyItem WHERE date=date('now')  OR (date=date('now','+1 day') AND time<'08:00')")
    fun toNotify():MutableList<MyItem>

    @Query("INSERT INTO MyItem(name,image,date,time,info,important) VALUES (:name,:image,:date,:time,:info,:important)")
    fun insert(name:String,image:Int,date:String,time:String,info:String,important:Boolean)
}