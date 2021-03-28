package com.example.faceplant.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.faceplant.R
import com.example.faceplant.activities.MySeeds.MySeedsActivity
import com.example.faceplant.activities.myPlants.MyPlantsActivity
import com.example.faceplant.activities.plantCare.PlantCareActivity
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.User
import com.example.faceplant.utils.Constants
import com.example.faceplant.utils.SharedPrefsClass
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException


class UserProfileActivity : AppCompatActivity() {
    private var userImageUri: Uri? = null
    private var userImageURL: String = ""
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val usernameTextView = findViewById<TextView>(R.id.user_profile_usernameTextView)
        val emailTextView = findViewById<TextView>(R.id.user_profile_emailTextView)
        val signOutButton = findViewById<Button>(R.id.user_profile_signOutButton)
        val userImage = findViewById<ImageView>(R.id.user_profile_imageView)

        // Check if user details are stored in SharedPref
        if(SharedPrefsClass().getSharedPreference(
                this, Constants.USER_PREFS, Constants.USERNAME_PREF_KEY, null) != null &&
            SharedPrefsClass().getSharedPreference(
                this, Constants.USER_PREFS, Constants.USER_EMAIL, null) != null){

            usernameTextView.text = SharedPrefsClass().getSharedPreference(
                this, Constants.USER_PREFS, Constants.USERNAME_PREF_KEY, "")

            emailTextView.text = SharedPrefsClass().getSharedPreference(
                this, Constants.USER_PREFS, Constants.EMAIL_PREF_KEY, "")

            userImageURL = SharedPrefsClass().getSharedPreference(
                this, Constants.USER_PREFS, Constants.PROFILE_IMAGE_PREF_KEY, "").toString()

            FirestoreClass().glideUserImageLoader(this, userImageURL, userImage)

        }else{

            val userId = FirestoreClass().getUserId()
            if (userId != null) {
                db.collection(Constants.USERS).document(userId).get()
                    .addOnSuccessListener { document ->
                        // Converting document snapshot to User data model object
                        val user = document.toObject(User::class.java)!!
                        usernameTextView.text = user.username
                        emailTextView.text = user.email
                        userImageURL = user.image
                        FirestoreClass().glideUserImageLoader(this, userImageURL, userImage)
                    }
            }
        }


        userImage.setOnClickListener{
            // Check if permission for storage is granted
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                )== PackageManager.PERMISSION_GRANTED){
                    // Open gallery if permission is granted
                chooseImage()
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        // Sign out the user
        signOutButton.setOnClickListener(){
            Firebase.auth.signOut()
            SharedPrefsClass().clearSharedPreference(
                this, Constants.USER_PREFS, Constants.USERNAME_PREF_KEY
            )
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.user_profile_bottom_navigation)

        bottomNavigationView.selectedItemId = R.id.navigation_user_proflie
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_user_proflie -> {
                    startActivity(Intent(applicationContext, UserProfileActivity::class.java))
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
                R.id.navigation_plant_care -> {
                    startActivity(Intent(applicationContext, PlantCareActivity::class.java))
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.READ_STORAGE_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted
                    chooseImage()
                } else {

                    Toast.makeText(
                        this, R.string.message_access_to_storage_denied, Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }


    // Handling the image chooser activity result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            userImageUri = data!!.data
            try {
                FirestoreClass().glideUserImageLoader(this, userImageUri!!, user_profile_imageView)
                FirestoreClass().uploadImage(this, userImageUri, Constants.USERS)
                SharedPrefsClass().setSharedPreference(
                        this, Constants.USER_PREFS, Constants.PROFILE_IMAGE_PREF_KEY, userImageUri.toString()
                )
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, R.string.message_image_selection_failed, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Function to open storage for choosing image
    private fun chooseImage(){
        val intent = Intent(
            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, Constants.PICK_IMAGE_REQUEST_CODE)
    }
}