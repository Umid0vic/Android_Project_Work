package com.example.faceplant.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Making a Class parcelible makes it possible to pass complex object between activities
@Parcelize
class User (
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val image: String = ""
): Parcelable