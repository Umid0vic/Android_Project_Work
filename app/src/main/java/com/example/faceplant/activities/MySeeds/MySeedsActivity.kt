package com.example.faceplant.activities.MySeeds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.faceplant.R
import com.example.faceplant.activities.UserProfileActivity
import com.example.faceplant.activities.myPlants.AddPlantActivity
import com.example.faceplant.activities.myPlants.MyPlantsActivity
import com.example.faceplant.activities.myPlants.MyPlantsAdapter
import com.example.faceplant.activities.plantCare.PlantCareActivity
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.Plant
import com.example.faceplant.models.Seed
import com.example.faceplant.models.User
import com.example.faceplant.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_plants.*
import kotlinx.android.synthetic.main.activity_my_plants.topAppBar
import kotlinx.android.synthetic.main.activity_my_seeds.*

class MySeedsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_seeds)

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.my_seeds_bottom_navigation)

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add -> {
                    // Handle add icon press
                    startActivity(Intent(this, AddSeedActivity::class.java))
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.navigation_my_seeds
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_my_seeds -> {
                    startActivity(Intent(applicationContext, MySeedsActivity::class.java))
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
                R.id.navigation_plant_care -> {
                    startActivity(Intent(applicationContext, PlantCareActivity::class.java))
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

        getListFromFirestore()
    }

    override fun onResume() {
        super.onResume()

        getListFromFirestore()
    }

    fun getListFromFirestore(){
        FirestoreClass().getSeedList(this)
    }
    fun seedListDownloaded(seedList: ArrayList<Seed>){
        if(seedList.size > 0){

            findViewById<TextView>(R.id.no_seeds_added_textView).visibility = View.GONE
            mySeeds_recyclerView.visibility = View.VISIBLE
            // Prepare the recyclerView with GridLayout
            mySeeds_recyclerView.layoutManager = LinearLayoutManager(this)
            mySeeds_recyclerView.setHasFixedSize(true)
            // Create an instance of MyPlantListAdapter
            val mySeedsAdapter = MySeedsAdapter(this, seedList)
            // Adapter instance is set to the recyclerview to inflate the items.
            mySeeds_recyclerView.adapter = mySeedsAdapter
        }else{
            mySeeds_recyclerView.visibility = View.GONE
            findViewById<TextView>(R.id.no_seeds_added_textView).visibility = View.VISIBLE
        }
    }
}