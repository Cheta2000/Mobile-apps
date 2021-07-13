package com.example.mygallery.Recycler

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MyImage(
    val image:Int,
    var rate:Float
):Parcelable

