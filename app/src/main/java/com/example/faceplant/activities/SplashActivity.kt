package com.example.faceplant.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import com.example.faceplant.R
import com.example.faceplant.activities.MainActivity
import com.example.faceplant.activities.myPlants.MyPlantsActivity
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.User
import com.example.faceplant.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        @Suppress("DEPRECATION")
        //If user is signed in start the HomeActivity else start the MainActivity
        Handler().postDelayed(
            {
                if (currentUser != null) {
                    startActivity(Intent(this, MyPlantsActivity::class.java))
                    finish()
                    Log.i("SplashActivity", "Starting userProfileActivity")
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }, 1500
        )
    }
}