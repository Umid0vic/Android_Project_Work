package com.example.faceplant.activities.plantCare
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.faceplant.R
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.Plant
import com.example.faceplant.models.PlantCareModel
import com.example.faceplant.utils.Constants

class PlantCareProfileActivity : AppCompatActivity() {
    //ta emot data från myPlantsActivity och ta in bild och titel
    private lateinit var plantDetails: PlantCareModel
    private lateinit var plantImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_care_profile)

        //decaling variables
        val lightIcon = findViewById<ImageView>(R.id.plantcare_profile_lighticon)
        val waterIcon = findViewById<ImageView>(R.id.plantcare_profile_watericon)
        val nutritionIcon = findViewById<ImageView>(R.id.plantcare_profile_nutritionicon)
        val generalInfoIcon = findViewById<ImageView>(R.id.plantcare_profile_genralinfo)
        plantImage = findViewById(R.id.plantcare_profile_imageview)
        val plantTitle = findViewById<TextView>(R.id.plantcare_profile_title_textview)


        if (intent.hasExtra(Constants.PLANT_CARE_DETAILS)) {
            plantDetails = intent.getParcelableExtra(Constants.PLANT_CARE_DETAILS)!!

            FirestoreClass().glidePlantImageLoader(this, plantDetails.plantCareImage, plantImage)
            plantTitle.setText(plantDetails.plantCareTitle)
        }




        //handle user click on light icon
        lightIcon.setOnClickListener {
            val intent = Intent(this, PlantCare_LightActivity::class.java)
            intent.putExtra(Constants.LIGHT_INFO, plantDetails.plantCareLight)

            startActivity(intent)
        }


        //handle user click on water icon
        waterIcon.setOnClickListener {
            val intent = Intent(this, PlantCare_WaterActivity::class.java)
            intent.putExtra(Constants.WATER_INFO,plantDetails.plantCareWater)

            startActivity(intent)
        }

        //handle user click on nutrition icon
        nutritionIcon.setOnClickListener {
            val intent = Intent(this, PlantCare_NutritionActivity::class.java)
            intent.putExtra(Constants.NUTRITION_INFO,plantDetails.plantCareNutrition)

            startActivity(intent)

        }

        //handle user click on generalinfo icon
        generalInfoIcon.setOnClickListener {
            val intent = Intent(this, PlantCare_GeneralInfoActivity::class.java)
            intent.putExtra(Constants.GENERAL_INFO,plantDetails.plantCareGeneralInfo)

            startActivity(intent)

        }

    }

}