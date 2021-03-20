package com.example.faceplant.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Plant (
        val userId: String = "",
        val plantType: String = "",
        val dateOfPurchase: String = "",
        val plantHealth: String = "",
        val moreAboutPlant: String = "",
        val plantImage: String = "",
        var plantId: String = ""
): Parcelable