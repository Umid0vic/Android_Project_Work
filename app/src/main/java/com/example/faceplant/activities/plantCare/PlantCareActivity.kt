package com.example.faceplant.activities.plantCare
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.faceplant.R
import com.example.faceplant.activities.MySeeds.MySeedsActivity
import com.example.faceplant.activities.SignInActivity
import com.example.faceplant.activities.UserProfileActivity
import com.example.faceplant.activities.myPlants.MyPlantsActivity
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.PlantCareModel
import com.example.faceplant.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_plant_care.*
import java.util.*
import kotlin.collections.ArrayList


class PlantCareActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_care)

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.plant_care_bottom_navigation)

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
                    // Check if user is signed in. If not show the SignInnDialog
                    if(currentUser != null) {
                        startActivity(Intent(applicationContext, MyPlantsActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }else{
                        showDialogToSignIn()
                    }
                        return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_my_seeds -> {
                    // Check if user is signed in. If not show the SignInnDialog
                    if(currentUser != null) {
                        startActivity(Intent(applicationContext, MySeedsActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }else{
                        showDialogToSignIn()
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_user_proflie -> {
                    // Check if user is signed in. If not show the SignInnDialog
                    if(currentUser != null) {
                        startActivity(Intent(applicationContext, UserProfileActivity::class.java))
                        overridePendingTransition(0, 0)
                        finish()
                    }else{
                        showDialogToSignIn()
                    }
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
            plant_care_recyclerview.layoutManager = GridLayoutManager(this, 2)

            //creating an instance of Plantcareadapter, assigning it to the recyclerview
            plant_care_recyclerview.setHasFixedSize(true)
            val adapterPlantCare = PlantCareAdapter(this, listPlantCare)
            plant_care_recyclerview.adapter = adapterPlantCare
        }
    }

    private fun showDialogToSignIn(){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.sign_in_to_continue_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.message_sign_in_to_continue))
        builder.setIcon(android.R.drawable.ic_lock_lock)

        builder.setPositiveButton(resources.getString(R.string.sign_in)) { dialogInterface, _ ->
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            dialogInterface.dismiss()
            finish()
        }

        builder.setNegativeButton(resources.getString(R.string.cancel)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}