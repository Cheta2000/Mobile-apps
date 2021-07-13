package com.example.to_do.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class MyItem(
    @PrimaryKey val id:Int?,
    @ColumnInfo(name="name") val name:String?,
    @ColumnInfo(name="image") val image:Int?,
    @ColumnInfo(name="date") val date:String?,
    @ColumnInfo(name="time") val time:String?,
    @ColumnInfo(name="info") val info:String?,
    @ColumnInfo(name="important") val important:Boolean?
)