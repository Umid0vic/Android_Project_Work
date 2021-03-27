package com.example.faceplant.activities.MySeeds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.faceplant.R
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.Seed
import kotlinx.android.synthetic.main.activity_add_plant.*
import kotlinx.android.synthetic.main.activity_add_seed.*

class AddSeedActivity : AppCompatActivity() {
    private lateinit var seedTypeEditText: EditText
    private lateinit var seedDateEditText: EditText
    private lateinit var moreAboutSeedEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_seed)


        seedTypeEditText = findViewById(R.id.add_seed_seedTypeEditText)
        seedDateEditText = findViewById(R.id.add_seed_seedDateEditText)
        moreAboutSeedEditText = findViewById(R.id.add_seed_moreAboutSeedEditText)
        val saveButton = findViewById<Button>(R.id.add_seed_saveButton)

        saveButton.setOnClickListener{
            // Validate entered plant details and upload the selected image to Firestore
            if(validateSeedDetails()){
                uploadSeedDetails()
                finish()
            }
        }
    }

    // Function to validate the seed details.
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
                true
            }
        }
    }

    private fun uploadSeedDetails(){
        val seed = Seed(
            FirestoreClass().getUserId()!!,
            seedTypeEditText.text.toString().trim {it <= ' '},
            seedDateEditText.text.toString().trim {it <= ' '},
            moreAboutSeedEditText.text.toString().trim {it <= ' '}
        )
        FirestoreClass().registerSeedDetails(this, seed)
        finish()
    }
}