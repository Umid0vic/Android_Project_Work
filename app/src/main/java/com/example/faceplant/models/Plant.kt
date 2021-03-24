package com.example.faceplant.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Plant (
        val userId: String = "",
        var plantType: String = "",
        var dateOfPurchase: String = "",
        var plantHealth: String = "",
        var moreAboutPlant: String = "",
        var plantImage: String = "",
        var plantId: String = ""
): Parcelable