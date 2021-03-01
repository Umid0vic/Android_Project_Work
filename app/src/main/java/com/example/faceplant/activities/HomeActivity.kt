package com.example.faceplant.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.faceplant.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.math.sign

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val signOutButton = findViewById<Button>(R.id.sign_out_button)

        signOutButton.setOnClickListener(){
            Firebase.auth.signOut()
        }
    }
}