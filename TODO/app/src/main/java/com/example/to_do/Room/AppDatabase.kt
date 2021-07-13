package com.example.to_do.Room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MyItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
