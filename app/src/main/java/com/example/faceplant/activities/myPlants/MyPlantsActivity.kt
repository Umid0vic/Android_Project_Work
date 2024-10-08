package com.example.faceplant.activities.myPlants

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.faceplant.R
import com.example.faceplant.activities.MySeeds.MySeedsActivity
import com.example.faceplant.activities.SignInActivity
import com.example.faceplant.activities.UserProfileActivity
import com.example.faceplant.activities.plantCare.PlantCareActivity
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.Plant
import com.example.faceplant.models.User
import com.example.faceplant.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_plants.*

class MyPlantsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_plants)

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add -> {
                    // Handle add icon press
                    startActivity(Intent(this, AddPlantActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.my_plants_bottom_navigation)

        bottomNavigationView.selectedItemId = R.id.navigation_my_plants
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_my_plants -> {
                    startActivity(Intent(applicationContext, MyPlantsActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_my_seeds -> {
                    startActivity(Intent(applicationContext, MySeedsActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_plant_care -> {
                    startActivity(Intent(applicationContext, PlantCareActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_user_proflie -> {
                    startActivity(Intent(applicationContext, UserProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }

        getPlantsFromFirestore()
    }

    override fun onResume() {
        super.onResume()

        getPlantsFromFirestore()
    }

    fun getPlantsFromFirestore(){
        FirestoreClass().getPlantList(this)
    }

    fun plantListDownloaded(plantList: ArrayList<Plant>){
        if(plantList.size > 0){

            findViewById<TextView>(R.id.no_plants_added_textView).visibility = View.GONE
            myPlants_recyclerView.visibility = View.VISIBLE
            // Prepare the recyclerView with GridLayout
            myPlants_recyclerView.layoutManager = GridLayoutManager(this, 2)
            myPlants_recyclerView.setHasFixedSize(true)
            // Create an instance of MyPlantListAdapter
            val myPlantsAdapter = MyPlantsAdapter(this, plantList)
            // Adapter instance is set to the recyclerview to inflate the items.
            myPlants_recyclerView.adapter = myPlantsAdapter
        }else{
            myPlants_recyclerView.visibility = View.GONE
            findViewById<TextView>(R.id.no_plants_added_textView).visibility = View.VISIBLE
        }
    }
}