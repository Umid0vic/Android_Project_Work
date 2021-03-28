package com.example.faceplant.activities.MySeeds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.faceplant.R
import com.example.faceplant.activities.myPlants.UpdatePlantActivity
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.Plant
import com.example.faceplant.models.Seed
import com.example.faceplant.utils.Constants

class ViewSeedActivity : AppCompatActivity() {

    private lateinit var updateSeedButton: Button
    private lateinit var removeSeedButton: Button
    private lateinit var seedDetails: Seed
    private lateinit var seedTypeEditText: EditText
    private lateinit var seedDateEditText: EditText
    private lateinit var moreAboutSeedEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_seed)

        updateSeedButton = findViewById(R.id.view_seed_updateButton)
        removeSeedButton = findViewById(R.id.view_seed_removeButton)
        seedTypeEditText = findViewById(R.id.view_seed_seedTypeEditText)
        seedTypeEditText.isEnabled = false
        seedDateEditText = findViewById(R.id.view_seed_seedDateEditText)
        seedDateEditText.isEnabled = false
        moreAboutSeedEditText = findViewById(R.id.view_seed_moreAboutSeedEditText)
        moreAboutSeedEditText.isEnabled = false

        // Check if intent has Extra
        if (intent.hasExtra(Constants.SEED_DETAILS)) {
            seedDetails = intent.getParcelableExtra(Constants.SEED_DETAILS)!!

            seedTypeEditText.setText(seedDetails.seedsType)
            seedDateEditText.setText(seedDetails.dateOfPurchase)
            moreAboutSeedEditText.setText(seedDetails.moreAboutSeeds)

            removeSeedButton.setOnClickListener{
                // Show alert dialog when clicking remove button
                showAlertDialogToRemove(seedDetails.seedId)
            }

            updateSeedButton.setOnClickListener{
                val intent = Intent(this, UpdateSeedActivity::class.java)
                intent.putExtra(Constants.SEED_DETAILS, seedDetails)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showAlertDialogToRemove(plantId: String){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.remove_dialog_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.remove_dialog_message_seed))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->

            // Removes a plant with given plantId
            FirestoreClass().removeItem(plantId, Constants.SEEDS)
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