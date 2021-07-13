package com.example.arkanoidgame

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Progress::class], version = 1)
abstract class MyDatabase: RoomDatabase() {
    abstract fun progressDAO():ProgressDAO

}