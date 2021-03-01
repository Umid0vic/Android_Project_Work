package com.example.faceplant.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.faceplant.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

   // private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // auth = Firebase.auth
        val buttonSignIn = findViewById<Button>(R.id.btn_start_page_sign_in)
        val buttonSignInLater = findViewById<Button>(R.id.btn_start_page_sign_in_later)

        buttonSignIn.setOnClickListener{
            //Launch the SignInActivity when the Sign in button clicked
            startActivity(
                Intent(this, SignInActivity::class.java)
            )
        }

        buttonSignInLater.setOnClickListener{
            startActivity(
                Intent(this, HomeActivity::class.java)
            )
        }
    }
}
