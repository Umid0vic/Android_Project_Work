package com.example.faceplant.firestore

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.faceplant.R
import com.example.faceplant.activities.SignInActivity
import com.example.faceplant.activities.SplashActivity
import com.example.faceplant.activities.UserProfileActivity
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


class FirestoreClass {

    private val db = FirebaseFirestore.getInstance()
    var storageRef = Firebase.storage.reference

    //Function to save user info in Firestore
    fun registerUser(userInfo: User){
        //Sets the data with userInfo under the collection named "users". Document id is users id.
        db.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
    }

    //Function to get user info from Firestore
    fun getUserInfo(activity: Activity){

        db.collection(Constants.USERS)
            .document(getUserId())
            .get()
            .addOnSuccessListener { document->
                // Converting document snapshot to User data model object
                val user = document.toObject(User::class.java)
                val sharedPreferences = activity.getSharedPreferences(
                    Constants.FACEPLANT_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                // Saving username inside the sharedPreferences key: UsernamePrefKey
                editor.putString(Constants.USERNAME_PREF_KEY, user?.username)
                editor.apply()

                when(activity){
                    is SignInActivity -> {
                        if (user != null) {
                            activity.userSignInSuccess(user)
                        }
                    }
                    is SplashActivity -> {
                        if (user != null) {
                            activity.userAlreadySignedIn(user)
                        }
                    }
                }
            }
    }


    //Function to update user info
    fun updateUserInfo(userHashmap: HashMap<String, Any>){
        db.collection(Constants.USERS)
            .document(getUserId())
            .update(userHashmap)
            .addOnSuccessListener {
            }
            .addOnFailureListener{
            }
    }

    fun getUserId(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null){
            currentUserId = currentUser.uid
        }
        return currentUserId
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
        db.collection(Constants.USERS).document(getUserId())
            .update(mapOf(
                Constants.USER_IMAGE to uri
            ))
    }
}