package com.example.faceplant.activities.myPlants

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_plant.*
import kotlinx.android.synthetic.main.activity_add_plant.add_plant_plantImage
import kotlinx.android.synthetic.main.activity_view_plant.*
import java.io.IOException

class ViewPlantActivity : AppCompatActivity() {

    // A global variable for URI of a selected image from phone storage.
    private var selectedImageUri: Uri? = null
    // A global variable for uploaded plant image URL.
    private var plantImageURL: String = ""
    private lateinit var updatePlantButton: Button
    private lateinit var removePlantButton: Button
    private lateinit var saveUpdatesButton: Button
    private lateinit var updateImageIcon: ImageView
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
        saveUpdatesButton = findViewById(R.id.view_plant_saveUpdatesButton)
        updateImageIcon = findViewById(R.id.update_image_icon)

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
        }

        updatePlantButton.setOnClickListener{
            // Sets editTexts for plant true
            letUserUpdate()
        }

        saveUpdatesButton.setOnClickListener(){
            // Validate entered plant details and upload the selected image to Firestore
            if(validatePlantDetails()){
                FirestoreClass().updatePlantDetails(plantDetails)
            }
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
    }

    private fun letUserUpdate() {

        updatePlantButton.isVisible = false
        removePlantButton.isVisible = false
        updateImageIcon.isVisible = true
        saveUpdatesButton.isVisible = true
        plantTypeEditText.isEnabled = true
        plantDateEditText.isEnabled = true
        plantHealthEditText.isEnabled = true
        moreAboutPlantEditText.isEnabled = true

        Toast.makeText(
                this,
                R.string.message_you_can_edit_now,
                Toast.LENGTH_LONG ).show()

    }

    // Function to validate the plant details.
    private fun validatePlantDetails(): Boolean{
        return when {

            TextUtils.isEmpty(view_plant_plantTypeEditText.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, R.string.message_enter_plant_type, Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                plantDetails.plantImage = plantImageURL
                plantDetails.plantType = plantTypeEditText.text.toString().trim { it <= ' ' }
                plantDetails.dateOfPurchase = plantTypeEditText.text.toString().trim { it <= ' ' }
                plantDetails.plantHealth = plantTypeEditText.text.toString().trim { it <= ' ' }
                plantDetails.moreAboutPlant = plantTypeEditText.text.toString().trim { it <= ' ' }
                true
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
            FirestoreClass().removeItem(plantId)
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

    // Function to open storage for choosing image
    private fun chooseImage(){
        val intent = Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, Constants.PICK_IMAGE_REQUEST_CODE)
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
                FirestoreClass().glidePlantImageLoader(this, selectedImageUri!!, view_plant_plantImage)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, R.string.message_image_selection_failed, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}