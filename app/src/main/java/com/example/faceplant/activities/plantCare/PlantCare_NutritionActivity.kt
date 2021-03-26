package com.example.faceplant.activities.plantCare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.faceplant.R
import com.example.faceplant.utils.Constants

class PlantCare_NutritionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_care__nutrition)
        val plantNutritionTxt = findViewById<TextView>(R.id.NutritionActivity_textview)

        if (intent.hasExtra(Constants.NUTRITION_INFO)) {
            plantNutritionTxt.setText(intent.getStringExtra(Constants.NUTRITION_INFO))

        }
    }
}