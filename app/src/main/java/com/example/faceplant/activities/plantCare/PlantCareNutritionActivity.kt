package com.example.faceplant.activities.plantCare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.faceplant.R
import com.example.faceplant.utils.Constants

class PlantCareNutritionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_care_nutrition)
        val plantNutritionTxt = findViewById<TextView>(R.id.plant_care_nutrition_textView)

        if (intent.hasExtra(Constants.NUTRITION_INFO)) {
            plantNutritionTxt.text = intent.getStringExtra(Constants.NUTRITION_INFO)
        }
    }
}