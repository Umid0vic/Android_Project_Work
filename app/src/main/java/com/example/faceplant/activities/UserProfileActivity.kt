package com.example.faceplant.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.faceplant.R
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.User
import com.example.faceplant.utils.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException
import java.io.InputStream
import java.net.URL


class UserProfileActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var userDetails: User
    private var userImageUri: Uri? = null
    private var userImageURL: String =""
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    var imagesRef: StorageReference? = storageRef.child("images")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val signOutButton = findViewById<Button>(R.id.profile_sign_out_button)
        val userImage = findViewById<ImageView>(R.id.profile_image)

        usernameEditText = findViewById(R.id.sign_up_editTextUsername)
        usernameEditText.isEnabled = false
        emailEditText = findViewById(R.id.sign_in_editTextEmail)
        emailEditText.isEnabled = false

        if(intent.hasExtra(Constants.USER_DETAILS)){
            userDetails = intent.getParcelableExtra(Constants.USER_DETAILS)!!
            usernameEditText.setText(userDetails.username)
            emailEditText.setText(userDetails.email)
            if(userDetails.image != null) {
                userImageURL = userDetails.image
                FirestoreClass().glideImageLoader(this, userImageURL, profile_image)
            }
            Log.i("UserProfileActivity", "Checking if user if signed in")
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
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

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

            // Add other 'when' lines to check for other
            // permissions this app might request.
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
           //     profile_image.setImageURI(userImageUri)
                FirestoreClass().glideImageLoader(this, userImageUri!!, profile_image)
                FirestoreClass().uploadImage(this, userImageUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun chooseImage(){
        val intent = Intent(
            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, Constants.PICK_IMAGE_REQUEST_CODE)
    }

    fun updateUserImage(imageURL: String){
        userImageURL = imageURL
    }
}

///*
//    private fun showFileChooser() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(
//            Intent.createChooser(intent, "Select Picture"),
//            Constants.PICK_IMAGE_REQUEST_CODE
//        )
//    }
//
// */