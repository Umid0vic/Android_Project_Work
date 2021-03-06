package com.example.faceplant.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityManagerCompat
import androidx.core.content.ContextCompat
import com.example.faceplant.R
import com.example.faceplant.models.User
import com.example.faceplant.utils.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.jar.Manifest

class UserProfileActivity : AppCompatActivity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextLocation: EditText
    private lateinit var editTextProfileBio: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val signOutButton = findViewById<Button>(R.id.profile_sign_out_button)
        val editProfileButton = findViewById<Button>(R.id.profile_edit_Btn)
        val userImage = findViewById<ImageView>(R.id.profile_image)

        editTextUsername = findViewById(R.id.sign_up_editTextUsername)
        editTextUsername.isEnabled = false
        editTextEmail = findViewById(R.id.sign_in_editTextEmail)
        editTextEmail.isEnabled = false
        editTextLocation = findViewById(R.id.sign_in_editTextPassword)
        editTextLocation.isEnabled = false
        editTextProfileBio = findViewById(R.id.sign_up_editTextRepeatPassword)
        editTextProfileBio.isEnabled = false

        var userDetails: User = User()
        if(intent.hasExtra(Constants.USER_DETAILS)){
            userDetails = intent.getParcelableExtra(Constants.USER_DETAILS)!!

            editTextUsername.setText(userDetails.username)
            editTextEmail.setText(userDetails.email)
        }

        userImage.setOnClickListener{
            // Check if permission is granted
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                )== PackageManager.PERMISSION_GRANTED){
                //Do something
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        editProfileButton.setOnClickListener{

        }

        signOutButton.setOnClickListener(){
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        when (requestCode) {
            Constants.READ_STORAGE_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted

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
}