package com.example.faceplant.activities.plantCare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.faceplant.R
import com.example.faceplant.utils.Constants

class PlantCare_GeneralInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_care__general_info)
        val plantGeneralInfoTxt = findViewById<TextView>(R.id.GeneralInfoActivity_textview)

        if (intent.hasExtra(Constants.GENERAL_INFO)) {
            plantGeneralInfoTxt.setText(intent.getStringExtra(Constants.GENERAL_INFO))
        }
    }
}