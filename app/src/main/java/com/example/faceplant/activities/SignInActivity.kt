package com.example.faceplant.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.faceplant.R

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val signInButton = findViewById<Button>(R.id.sign_in_page_sign_in)

        signInButton.setOnClickListener{
            startActivity(
                Intent(this, HomeActivity::class.java)
            )
        }

        val signUpText = findViewById<TextView>(R.id.sign_up_text) as TextView

        signUpText.setOnClickListener{
            startActivity(
                Intent(this, SignUpActivity::class.java)
            )
        }
    }
}