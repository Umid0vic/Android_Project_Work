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

        val loginBtn = findViewById<Button>(R.id.btn_start_page_sign_in)

        loginBtn.setOnClickListener{
            //Launch the SignInActivity when the Sign in button clicked
            startActivity(
                Intent(this, SignInActivity::class.java)
            )
        }
    }
}