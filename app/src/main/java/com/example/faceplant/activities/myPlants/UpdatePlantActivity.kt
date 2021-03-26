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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.faceplant.R
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.Plant
import com.example.faceplant.utils.Constants
import kotlinx.android.synthetic.main.activity_update_plant.*

import java.io.IOException

class UpdatePlantActivity : AppCompatActivity() {

    // A global variable for URI of a selected image from phone storage.
    private var selectedImageUri: Uri? = null
    // A global variable for uploaded plant image URL.
    private var plantImageURL: String = ""
    private lateinit var updateImageIcon: ImageView
    private lateinit var saveUpdatesButton: Button
    private lateinit var plantDetails: Plant
    private lateinit var plantImage: ImageView
    private lateinit var plantTypeEditText: EditText
    private lateinit var plantDateEditText: EditText
    private lateinit var plantHealthEditText: EditText
    private lateinit var moreAboutPlantEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_plant)

        saveUpdatesButton = findViewById(R.id.update_plant_saveUpdatesButton)
        updateImageIcon = findViewById(R.id.update_image_icon)
        plantImage = findViewById(R.id.update_plant_plantImage)
        plantTypeEditText = findViewById(R.id.update_plant_plantTypeEditText)
        plantDateEditText = findViewById(R.id.update_plant_plantDateEditText)
        plantHealthEditText = findViewById(R.id.update_plant_plantHealthEditText)
        moreAboutPlantEditText = findViewById(R.id.update_plant_moreAboutPlantEditText)

        // Check if intent has Extra and get plant details
        if (intent.hasExtra(Constants.PLANT_DETAILS)) {
            plantDetails = intent.getParcelableExtra(Constants.PLANT_DETAILS)!!

            plantImageURL = plantDetails.plantImage
            FirestoreClass().glidePlantImageLoader(this, plantImageURL, plantImage)
            plantTypeEditText.setText(plantDetails.plantType)
            plantDateEditText.setText(plantDetails.dateOfPurchase)
            plantHealthEditText.setText(plantDetails.plantHealth)
            moreAboutPlantEditText.setText(plantDetails.moreAboutPlant)
        }


        updateImageIcon.setOnClickListener{
            // Check if permission for storage is granted
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                )== PackageManager.PERMISSION_GRANTED){
                // Open gallery if permission is granted
                chooseImage()
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        saveUpdatesButton.setOnClickListener(){
            // Validate entered plant details and upload the selected image to Firestore
            if(validatePlantDetails()){
                FirestoreClass().updatePlantDetails(plantDetails)
                if(selectedImageUri.toString() != ""){
                    FirestoreClass().updatePlantImage(plantDetails.plantId, selectedImageUri.toString())
                }
            }
            finish()
        }
    }

    // Function to validate the plant details.
    private fun validatePlantDetails(): Boolean{
        return when {

            TextUtils.isEmpty(plantTypeEditText.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, R.string.message_enter_plant_type, Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
              //  if(selectedImageUri.toString() != ""){
              //      plantDetails.plantImage = selectedImageUri.toString() }
                plantDetails.plantType = plantTypeEditText.text.toString().trim { it <= ' ' }
                plantDetails.dateOfPurchase = plantDateEditText.text.toString().trim { it <= ' ' }
                plantDetails.plantHealth = plantHealthEditText.text.toString().trim { it <= ' ' }
                plantDetails.moreAboutPlant = moreAboutPlantEditText.text.toString().trim { it <= ' ' }
                true
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.READ_STORAGE_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted
                    chooseImage()
                } else {

                    Toast.makeText(
                        this, R.string.message_access_to_storage_denied, Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    // Handling the image chooser activity result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            selectedImageUri = data!!.data

            try {
                // Try to load the selected image
                FirestoreClass().glidePlantImageLoader(this, selectedImageUri!!, update_plant_plantImage)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, R.string.message_image_selection_failed, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Function to open storage for choosing image
    private fun chooseImage(){
        val intent = Intent(
            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, Constants.PICK_IMAGE_REQUEST_CODE)
    }
}