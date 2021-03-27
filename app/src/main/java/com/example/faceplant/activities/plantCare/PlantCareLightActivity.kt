package com.example.faceplant.activities.plantCare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.faceplant.R
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.utils.Constants

class PlantCareLightActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_care_light)
        val plantLightTxt = findViewById<TextView>(R.id.lightActivity_textview)

        if (intent.hasExtra(Constants.LIGHT_INFO)) {
            plantLightTxt.text = intent.getStringExtra(Constants.LIGHT_INFO)
        }

    }
}