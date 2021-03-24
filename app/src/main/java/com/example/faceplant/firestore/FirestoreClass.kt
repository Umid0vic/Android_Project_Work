package com.example.faceplant.firestore

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.example.faceplant.R
import com.example.faceplant.activities.*
import com.example.faceplant.activities.myPlants.AddPlantActivity
import com.example.faceplant.activities.myPlants.MyPlantsActivity
import com.example.faceplant.models.Plant
import com.example.faceplant.models.User
import com.example.faceplant.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.IOException


class FirestoreClass : AppCompatActivity()  {

    private val db = FirebaseFirestore.getInstance()
    private var storageRef = Firebase.storage.reference
    private val auth = FirebaseAuth.getInstance()


    //Function to save user info in Firestore
    fun registerUserDetails(context: Context, userInfo: User){
        //Sets the data with userInfo under the collection named "users". Document id is users id.
        db.collection(Constants.USERS)
                .document(userInfo.id)
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(
                            context, "Registered successfully", Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Log.e(
                            context.javaClass.simpleName, "Error while registering the details.", e
                    )
                }
    }

    //Function to save plant info in Firestore
    fun registerPlantDetails(context: Context, plantInfo: Plant){
        //Sets the data with userInfo under the collection named "users". Document id is users id.
        db.collection(Constants.PLANTS)
                .document()
                .set(plantInfo, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(
                            context, "Details are uploaded successfully", Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->

                    Log.e(
                            context.javaClass.simpleName, "Error while uploading the product details.", e
                    )
                }
    }

    // Function to get plant list from database
    fun getPlantList(activity: Activity){
        db.collection(Constants.PLANTS)
                .whereEqualTo(Constants.USER_ID, getUserId())
                .get()
                .addOnSuccessListener {document ->
                    val plantList: ArrayList<Plant> = ArrayList()
                    // Get plant documents from Firebase and add them to plantList
                    for (i in document.documents){
                        val plant = i.toObject(Plant::class.java)
                        plant!!.plantId = i.id
                        Log.i("PlantId", i.id)
                        Log.i("Modelid", plant.plantId)
                        plantList.add(plant)
                    }
                    when(activity){
                        is MyPlantsActivity -> {
                            activity.plantListDownloaded(plantList)
                        }
                    }
                }
    }

    fun getUserInfo(activity: Context): User?{
        var user = User()
        val userId = getUserId()
        if (userId != null){
            db.collection(Constants.PLANTS)
                    .document(userId)
                    .get()
                    .addOnSuccessListener { document ->
                    // Converting document snapshot to User data model object
                    user = document.toObject(User::class.java)!!
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

    fun glideUserImageLoader(context: Context, image: Any, imageView: ImageView){
        try{
            Glide
                .with(context)
                .load(image)
         //       .circleCrop()
                .placeholder(R.drawable.user_default_image)
                .into(imageView)
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    fun glidePlantImageLoader(context: Context, image: Any, imageView: ImageView){
        try{
            Glide
                    .with(context)
                    .load(image)
                    //       .circleCrop()
                    .placeholder(R.drawable.ic_plant_image)
                    .into(imageView)
        }catch (e: IOException){
            e.printStackTrace()
        }
    }


    // Function to upload image to Firestore
    fun uploadImage(activity: Activity, uri: Uri?, imageType: String) {
        // Reference to Firebase storage for uploading objects
        val riversRef : StorageReference =
                if(imageType == Constants.USERS){
                    storageRef.child("images/users/" + imageType + getUserId())
                }else{
                    storageRef.child("images/plants/" + imageType + System.currentTimeMillis())
                }

        riversRef.putFile(uri!!)
             .addOnSuccessListener {taskSnapshot->
                 taskSnapshot.metadata!!.reference!!.downloadUrl
                     .addOnSuccessListener {uri->
                         when(activity){
                                // Update userprofile with the image
                             is UserProfileActivity->{
                                 updateUserImage(uri.toString())
                             }
                             is AddPlantActivity->{
                                 activity.imageUploadSucces(uri.toString())
                             }
                         }
                     }
                    //if the upload is successful
                    Log.i("SignUpActivity", R.string.file_uploaded_successfully.toString())
             }
             .addOnFailureListener {

             }
    }

    fun updatePlantDetails(plantDetails: Plant){
        db.collection(Constants.USERS).document(plantDetails.plantId)
                .update(mapOf(
                        Constants.PLANT_IMAGE to plantDetails.plantImage,
                        Constants.PLANT_TYPE to plantDetails.plantType,
                        Constants.PLANT_DATE to plantDetails.dateOfPurchase,
                        Constants.PLANT_HEALTH to plantDetails.plantHealth,
                        Constants.MORE_ABOUT_PLANT to plantDetails.moreAboutPlant
                ))
    }

    fun updateUserImage(uri: String){
        getUserId()?.let {
            db.collection(Constants.USERS).document(it)
                .update(mapOf(
                    Constants.USER_IMAGE to uri
                ))
        }
    }

    // Function to remove an item from FireStore
    fun removeItem(plantId: String){
        db.collection(Constants.PLANTS).document(plantId)
                .delete()
                .addOnSuccessListener { Log.d("remove plant succes", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w("remove plant fail", "Error deleting document", e) }
    }
}