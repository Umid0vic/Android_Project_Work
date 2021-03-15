package com.example.faceplant.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.faceplant.R
import com.example.faceplant.utils.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import kotlin.math.sign

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val textView = findViewById<TextView>(R.id.hello_username)
        val sharedPreferences = getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.USERNAME_PREF_KEY, " ")

        textView.text = "hello $username"
        val signOutButton = findViewById<Button>(R.id.sign_out_button)

        signOutButton.setOnClickListener(){
            Firebase.auth.signOut()
        }
    }
}