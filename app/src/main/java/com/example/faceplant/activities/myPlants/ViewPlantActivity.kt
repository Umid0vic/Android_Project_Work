package com.example.faceplant.activities.myPlants

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.faceplant.R
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.Plant
import com.example.faceplant.utils.Constants
import kotlinx.android.synthetic.main.activity_add_plant.*
import kotlinx.android.synthetic.main.activity_view_plant.*
import java.io.IOException

class ViewPlantActivity : AppCompatActivity() {
    private lateinit var updatePlantButton: Button
    private lateinit var removePlantButton: Button

    private var plantImageURL: String = ""
    private lateinit var plantDetails: Plant
    private lateinit var plantImage: ImageView
    private lateinit var plantTypeEditText: EditText
    private lateinit var plantDateEditText: EditText
    private lateinit var plantHealthEditText: EditText
    private lateinit var moreAboutPlantEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_plant)

        updatePlantButton = findViewById(R.id.view_plant_updateButton)
        removePlantButton = findViewById(R.id.view_plant_removeButton)
        plantImage = findViewById(R.id.view_plant_plantImage)
        plantTypeEditText = findViewById(R.id.view_plant_plantTypeEditText)
        plantTypeEditText.isEnabled = false
        plantDateEditText = findViewById(R.id.view_plant_plantDateEditText)
        plantDateEditText.isEnabled = false
        plantHealthEditText = findViewById(R.id.view_plant_plantHealthEditText)
        plantHealthEditText.isEnabled = false
        moreAboutPlantEditText = findViewById(R.id.view_plant_moreAboutPlantEditText)
        moreAboutPlantEditText.isEnabled = false

        // Check if intent has Extra and get plant details
        if (intent.hasExtra(Constants.PLANT_DETAILS)) {
            plantDetails = intent.getParcelableExtra(Constants.PLANT_DETAILS)!!

            plantImageURL = plantDetails.plantImage
            plantTypeEditText.setText(plantDetails.plantType)
            plantDateEditText.setText(plantDetails.dateOfPurchase)
            plantHealthEditText.setText(plantDetails.plantHealth)
            moreAboutPlantEditText.setText(plantDetails.moreAboutPlant)
            FirestoreClass().glidePlantImageLoader(this, plantImageURL, plantImage)

            removePlantButton.setOnClickListener{
                // Show alert dialog when clicking remove button
                showAlertDialogToRemove(plantDetails.plantId)
            }

            updatePlantButton.setOnClickListener{
                val intent = Intent(this, UpdatePlantActivity::class.java)
                intent.putExtra(Constants.PLANT_DETAILS, plantDetails)
                startActivity(intent)
            }
        }
    }

    private fun showAlertDialogToRemove(plantId: String){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.remove_dialog_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.remove_dialog_message_plant))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->

            // Removes a plant with given plantId
            FirestoreClass().removeItem(plantId, Constants.PLANTS)
            dialogInterface.dismiss()
            finish()
        }

        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}