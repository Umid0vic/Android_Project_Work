package com.example.faceplant.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.faceplant.R
import com.example.faceplant.activities.plantCare.PlantCareActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // auth = Firebase.auth
        val signInButton = findViewById<Button>(R.id.btn_start_page_sign_in)
        val signInLaterButton = findViewById<Button>(R.id.btn_start_page_sign_in_later)

        signInButton.setOnClickListener{
            //Launch the SignInActivity when the Sign in button clicked
            startActivity(
                Intent(this, SignInActivity::class.java)
            )
        }

        signInLaterButton.setOnClickListener{

            startActivity(
                Intent(this, PlantCareActivity::class.java)
            )
            finish()
        }
    }
}
