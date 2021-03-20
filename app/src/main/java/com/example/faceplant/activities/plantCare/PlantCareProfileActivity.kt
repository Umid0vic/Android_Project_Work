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
        val lightFragment = PlantLightFragment()
        val waterFragment = PlantWaterFragment()
        val nutritionFragment = PlantNutritionFragment()
        val generalInfoFragment = PlantGeneralInfoFragment()


        // using a fragment transaction to switch between fragments when icons are clicked
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.plantcare_profile_framelayout_fragment, generalInfoFragment)
            commit()
        }

        //handle user clicks on the light icon by replacing the current fragment with lightfragment
        lightIcon.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.plantcare_profile_framelayout_fragment, lightFragment)
                addToBackStack(null)
                commit()
            }
        }

        //handle user clicks on the water icon by replacing the current fragment with waterfragment
        waterIcon.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.plantcare_profile_framelayout_fragment, waterFragment)
                addToBackStack(null)
                commit()
            }
        }

        //handle user clicks on the nutrition icon by replacing the current fragment with nutritionfragment
        nutritionIcon.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.plantcare_profile_framelayout_fragment, nutritionFragment )
                addToBackStack(null)
                commit()
            }
        }

        //handle user clicks on the General info icon by replacing the current fragment with generalInfoFragment
        generalInfoIcon.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.plantcare_profile_framelayout_fragment, generalInfoFragment )
                addToBackStack(null)
                commit()
            }
        }


    }
}