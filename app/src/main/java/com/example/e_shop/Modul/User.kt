package com.example.e_shop.Modul

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val id: String = " ",
    val firstname: String = "",
    val lastname: String = "",
    val email: String = " ",
    val mobile: Long = 0,
    val gender: String = "",
    val profileCompleted:Int = 0
    ):Parcelable