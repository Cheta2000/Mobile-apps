package com.example.tictactoe

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class User(val id:String?="", val name: String?="", val email: String?=""):Parcelable {

}