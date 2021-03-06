package com.example.faceplant.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import com.example.faceplant.R
import com.example.faceplant.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser


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
        //After the delay start the HomeActivity if user is signed in else start the MainActivity
        Handler().postDelayed(
            {
                if (user != null) {
                    startActivity(Intent(this, UserProfileActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }, 1500
        )
    }
}