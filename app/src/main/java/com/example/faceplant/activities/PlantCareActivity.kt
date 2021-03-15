package com.example.faceplant.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.faceplant.R
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.User
import com.example.faceplant.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class PlantCareActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_care)

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.selectedItemId = R.id.navigation_plant_care
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_plant_care -> {
                    startActivity(Intent(applicationContext, PlantCareActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_my_plants -> {
                    startActivity(Intent(applicationContext, MyPlantsActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_my_seeds -> {
                    startActivity(Intent(applicationContext, MySeedsActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_user_proflie -> {
                    startActivity(Intent(applicationContext, UserProfileActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }

    fun userSignInSuccess(user: User){
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra(Constants.USER_DETAILS, user)
        startActivity(intent)
        finish()
    }
}