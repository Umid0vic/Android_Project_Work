package com.example.faceplant.activities.plantCare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.faceplant.R
import com.example.faceplant.utils.Constants

class PlantCare_WaterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_care__water)
        val plantWaterTxt = findViewById<TextView>(R.id.WaterActivity_textview)

        if (intent.hasExtra(Constants.WATER_INFO)) {
            plantWaterTxt.setText(intent.getStringExtra(Constants.WATER_INFO))

        }
    }
}