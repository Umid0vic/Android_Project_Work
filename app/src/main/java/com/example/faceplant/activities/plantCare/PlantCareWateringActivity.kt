package com.example.faceplant.activities.plantCare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.faceplant.R
import com.example.faceplant.utils.Constants

class PlantCareWateringActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_care_watering)
        val plantWaterTxt = findViewById<TextView>(R.id.plant_care_watering_textView)

        if (intent.hasExtra(Constants.WATER_INFO)) {
            plantWaterTxt.text = intent.getStringExtra(Constants.WATER_INFO)

        }
    }
}