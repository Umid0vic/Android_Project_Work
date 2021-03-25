package com.example.faceplant.activities.plantCare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.faceplant.R
import kotlinx.android.synthetic.main.activity_plant_care_profile.*

class PlantCareProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_care_profile)

        //decaling variables for all clickable components
        val lightIcon = findViewById<ImageView>(R.id.plantcare_profile_lighticon)
        val waterIcon = findViewById<ImageView>(R.id.plantcare_profile_watericon)
        val nutritionIcon = findViewById<ImageView>(R.id.plantcare_profile_nutritionicon)
        val generalInfoIcon = findViewById<ImageView>(R.id.plantcare_profile_genralinfo)


        //declaring variables for the fragments








    }
}