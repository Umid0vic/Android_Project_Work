package com.example.faceplant.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.faceplant.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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