package com.example.faceplant.activities.plantCare
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.core.view.MenuItemCompat.getActionView
import androidx.core.view.MenuItemCompat.setOnActionExpandListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.faceplant.R
import com.example.faceplant.activities.MySeeds.MySeedsActivity
import com.example.faceplant.activities.UserProfileActivity
import com.example.faceplant.activities.myPlants.MyPlantsActivity
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.PlantCareModel
import com.example.faceplant.models.User
import com.example.faceplant.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_plant_care.*
import java.util.*
import kotlin.collections.ArrayList


class PlantCareActivity : AppCompatActivity() {

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

        //fetch the list from firestore
        FirestoreClass().getPlantCareDocuments(this)

    }

    override fun onResume() {
        super.onResume()

        FirestoreClass().getPlantCareDocuments(this)
    }

    fun getDownloadedPlantCareList(listPlantCare: ArrayList<PlantCareModel>){

        if(listPlantCare.size > 0 ){

            //To prepare the recyclerview with a gridlayoutmanager
            plantcare_recyclerview.layoutManager = GridLayoutManager(this, 2)

            //creating an instance of Plantcareadapter, assigning it to the recyclerview
            plantcare_recyclerview.setHasFixedSize(true)
            val adapterPlantCare = PlantCareAdapter(this, listPlantCare)
            plantcare_recyclerview.adapter = adapterPlantCare
        }

    }
}