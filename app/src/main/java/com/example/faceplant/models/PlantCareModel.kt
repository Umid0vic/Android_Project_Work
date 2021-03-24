package com.example.faceplant.models

import android.media.Image
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PlantCareModel(
    val plantCareImage: String = "",
    val plantCareTitle: String = "",
    val plantCareLight: String = "",
    val plantCareWater: String = "",
    val plantCareNutrition: String = "",
    val plantCareGeneralInfo: String = "",
    var plantCareId: String = ""
): Parcelable