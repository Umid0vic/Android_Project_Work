package com.example.faceplant.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Seed (
        val userId: String = "",
        var seedsType: String = "",
        var dateOfPurchase: String = "",
        var moreAboutSeeds: String = "",
        var seedId: String = ""
): Parcelable