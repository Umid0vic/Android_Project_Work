package com.example.faceplant.activities.plantCare
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.faceplant.R
import com.example.faceplant.activities.plantCare.PlantCareActivity
import com.example.faceplant.activities.MySeedsActivity
import com.example.faceplant.activities.UserProfileActivity
import com.example.faceplant.activities.myPlants.MyPlantsActivity
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.PlantCareModel
import com.example.faceplant.models.User
import com.example.faceplant.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_plant_care.*
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

        //calling function just to test if everything works
        FirestoreClass().getPlantCareDocuments(this)

    }


    fun userSignInSuccess(user: User){
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra(Constants.USER_DETAILS, user)
        startActivity(intent)
        finish()
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