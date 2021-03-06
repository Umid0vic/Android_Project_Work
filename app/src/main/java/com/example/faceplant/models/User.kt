package com.example.faceplant.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val image: String = ""
): Parcelable