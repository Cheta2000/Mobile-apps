package com.example.arkanoidgame

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Progress")
data class Progress(
    @PrimaryKey(autoGenerate=true) val moveID:Int?,
    @ColumnInfo(name="level") val level:Int,
    @ColumnInfo(name="x") val destroyedX:Int,
    @ColumnInfo(name="y") val destroyedY:Int,
    @ColumnInfo(name="color") val destroyedColor:String
) {
}