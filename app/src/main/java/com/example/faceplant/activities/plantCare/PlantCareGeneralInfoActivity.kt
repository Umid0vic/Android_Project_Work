package com.example.faceplant.activities.plantCare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.faceplant.R
import com.example.faceplant.utils.Constants

class PlantCareGeneralInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_care_general_info)
        val plantGeneralInfoTxt = findViewById<TextView>(R.id.plant_care_general_info_textView)

        if (intent.hasExtra(Constants.GENERAL_INFO)) {
            plantGeneralInfoTxt.text = intent.getStringExtra(Constants.GENERAL_INFO)
        }
    }
}