package com.example.faceplant.firestore

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.example.faceplant.R
import com.example.faceplant.activities.*
import com.example.faceplant.models.User
import com.example.faceplant.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.IOException


class FirestoreClass : AppCompatActivity()  {

    private val db = FirebaseFirestore.getInstance()
    private var storageRef = Firebase.storage.reference
    private val auth = FirebaseAuth.getInstance()
    private val userCollectionRef =  db.collection(Constants.USERS)


    //Function to save user info in Firestore
    fun registerUser(userInfo: User){
        //Sets the data with userInfo under the collection named "users". Document id is users id.
        userCollectionRef
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
    }

    fun getUserInfo(activity: Context): User?{
        var user = User()
        val userId = getUserId()
        if (userId != null){
            userCollectionRef.document(userId).get()
                .addOnSuccessListener { document ->
                    // Converting document snapshot to User data model object
                    user = document.toObject(User::class.java)!!
                    /*
                   val intent = Intent(activity.applicationContext, SignInActivity::class.java)
                   intent.putExtra(Constants.USERS, user)
                   startActivity(intent)

                   when (context) {
                       is SignInActivity -> {
                           if (user != null) {
                               context.userSignInSuccess(user)
                           }
                   */
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "get failed with ", exception)
                }
        }else{
            startActivity(Intent(activity.applicationContext, MainActivity::class.java))
        }
        return user
    }

    //Function to update user info
    fun updateUserInfo(userHashmap: HashMap<String, Any>){
        getUserId()?.let {
            db.collection(Constants.USERS)
                .document(it)
                .update(userHashmap)
                .addOnSuccessListener {
                }
                .addOnFailureListener{
                }
        }
    }

    fun getUserId(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null){
            currentUserId = currentUser.uid
            return currentUserId
        }
        return null
    }

    fun glideImageLoader(context: Context, image: Any, imageView: ImageView){
        try{
            Glide
                .with(context)
                .load(image)
                .placeholder(R.drawable.user_deafault_image)
                .into(imageView)
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    // Function to uppload image to Firestore
    fun uploadImage(activity: Activity, uri: Uri?) {
            val riversRef: StorageReference = storageRef.child("images/" + Constants.USER_PROFILE_IMAGE + getUserId())
            riversRef.putFile(uri!!)
                .addOnSuccessListener {taskSnapshot->
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {uri->
                            when(activity){
                                // Update userprofile with the image
                                is UserProfileActivity->{
                                    updateUserImage(uri.toString())
                                }
                            }
                        }
                    //if the upload is successful
                    Log.i("SignUpActivity", R.string.file_uploaded_successfully.toString())
                }
                .addOnFailureListener {
                }
        }

    fun updateUserImage(uri: String){
        getUserId()?.let {
            db.collection(Constants.USERS).document(it)
                .update(mapOf(
                    Constants.USER_IMAGE to uri
                ))
        }
    }
}