package com.example.faceplant.activities.MySeeds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.faceplant.R
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.Seed
import com.example.faceplant.utils.Constants
import kotlinx.android.synthetic.main.activity_add_seed.*
import kotlinx.android.synthetic.main.activity_update_plant.*
import kotlinx.android.synthetic.main.activity_update_plant.update_plant_plantTypeEditText
import kotlinx.android.synthetic.main.activity_update_seed.*

class UpdateSeedActivity : AppCompatActivity() {

    private lateinit var saveUpdatesButton: Button
    private lateinit var seedDetails: Seed
    private lateinit var seedTypeEditText: EditText
    private lateinit var seedDateEditText: EditText
    private lateinit var moreAboutSeedEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_seed)

        saveUpdatesButton = findViewById(R.id.update_seed_saveUpdatesButton)
        seedTypeEditText = findViewById(R.id.update_seed_seedTypeEditText)
        seedDateEditText = findViewById(R.id.update_seed_seedDateEditText)
        moreAboutSeedEditText = findViewById(R.id.update_seed_moreAboutSeedEditText)

        // Check if intent has Extra
        if (intent.hasExtra(Constants.SEED_DETAILS)) {
            seedDetails = intent.getParcelableExtra(Constants.SEED_DETAILS)!!

            seedTypeEditText.setText(seedDetails.seedsType)
            seedDateEditText.setText(seedDetails.dateOfPurchase)
            moreAboutSeedEditText.setText(seedDetails.moreAboutSeeds)
        }

        saveUpdatesButton.setOnClickListener(){
            // Validate entered seed details
            if(validateSeedDetails()){
                FirestoreClass().updateSeedDetails(seedDetails)
                finish()
            }
        }
    }

    // Function to validate the plant details.
    private fun validateSeedDetails(): Boolean{
        return when {

            TextUtils.isEmpty(seedTypeEditText.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, R.string.message_enter_seed_type, Toast.LENGTH_SHORT
                ).show()
                false
            }
            seedTypeEditText.text.toString().trim { it <= ' ' }.length >= 15 -> {
                Toast.makeText(
                        this, R.string.message_seed_type_is_long, Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                seedDetails.seedsType = seedTypeEditText.text.toString().trim { it <= ' ' }
                seedDetails.dateOfPurchase = seedDateEditText.text.toString().trim { it <= ' ' }
                seedDetails.moreAboutSeeds = moreAboutSeedEditText.text.toString().trim { it <= ' ' }
                true
            }
        }
    }
}