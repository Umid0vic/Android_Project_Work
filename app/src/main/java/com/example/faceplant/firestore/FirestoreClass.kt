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
import com.example.faceplant.R
import com.example.faceplant.activities.*
import com.example.faceplant.activities.MySeeds.MySeedsActivity
import com.example.faceplant.activities.myPlants.AddPlantActivity
import com.example.faceplant.activities.myPlants.MyPlantsActivity
import com.example.faceplant.activities.plantCare.PlantCareActivity
import com.example.faceplant.models.Plant
import com.example.faceplant.models.PlantCareModel
import com.example.faceplant.models.Seed
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

    //Function to save seed info in Firestore
    fun registerSeedDetails(context: Context, seedInfo: Seed){
        //Sets the data with seedInfo under the collection named "seeds".
        db.collection(Constants.SEEDS)
            .document()
            .set(seedInfo, SetOptions.merge())
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

    // Function to get plantlist from database
    fun getPlantList(activity: Activity){
        db.collection(Constants.PLANTS)
                .whereEqualTo(Constants.USER_ID, getUserId())
                .get()
                .addOnSuccessListener {document ->
                    val plantList: ArrayList<Plant> = ArrayList()
                    // Get plants documents from Firebase and add them to plantList
                    for (i in document.documents){
                        val plant = i.toObject(Plant::class.java)
                        plant!!.plantId = i.id
                        plantList.add(plant)
                    }
                    when(activity){
                        is MyPlantsActivity -> {
                            activity.plantListDownloaded(plantList)
                        }
                    }
                }
    }

    // Function to get plant list from database
    fun getSeedList(activity: Activity){
        db.collection(Constants.SEEDS)
            .whereEqualTo(Constants.USER_ID, getUserId())
            .get()
            .addOnSuccessListener {document ->
                val seedList: ArrayList<Seed> = ArrayList()
                // Get plant documents from Firebase and add them to plantList
                for (i in document.documents){
                    val seed = i.toObject(Seed::class.java)
                    seed!!.seedId = i.id
                    seedList.add(seed)
                }
                when(activity){
                    is MySeedsActivity -> {
                        activity.seedListDownloaded(seedList)
                    }
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
             }
             .addOnFailureListener {exception ->
                 Log.e(
                     activity.javaClass.simpleName,
                     exception.message,
                     exception
                 )
             }
    }

    fun updatePlantDetails(plantDetails: Plant){
        db.collection(Constants.PLANTS).document(plantDetails.plantId)
            .update(mapOf(
            //    Constants.PLANT_IMAGE to plantDetails.plantImage,
                Constants.PLANT_TYPE to plantDetails.plantType,
                Constants.PLANT_DATE to plantDetails.dateOfPurchase,
                Constants.PLANT_HEALTH to plantDetails.plantHealth,
                Constants.MORE_ABOUT_PLANT to plantDetails.moreAboutPlant
            ))
    }

    fun updateSeedDetails(seedDetails: Seed){
        db.collection(Constants.SEEDS).document(seedDetails.seedId)
            .update(mapOf(
                Constants.SEED_TYPE to seedDetails.seedsType,
                Constants.SEED_DATE to seedDetails.dateOfPurchase,
                Constants.MORE_ABOUT_SEED to seedDetails.moreAboutSeeds
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

    // function to get plant care documents
    fun getPlantCareDocuments(activity: Activity){

        db.collection(Constants.PLANT_CARE)
            .get()
            .addOnSuccessListener { documents ->
                val plantCareList: ArrayList<PlantCareModel> = ArrayList()
                for (i in documents) {
                    val plantCareItem = i.toObject(PlantCareModel::class.java)
                    plantCareItem.plantCareId = i.id
                    plantCareList.add(plantCareItem)

                    when(activity){
                        is PlantCareActivity -> {
                            activity.getDownloadedPlantCareList(plantCareList)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
            }
    }

    fun updatePlantImage(plantId: String, uri: String){
        db.collection(Constants.PLANTS).document(plantId)
                .update(mapOf(
                        Constants.PLANT_IMAGE to uri
                ))
    }

    // Function to remove an item from FireStore
    fun removeItem(plantId: String, collection: String){
        db.collection(collection).document(plantId)
                .delete()
                .addOnSuccessListener {  }
                .addOnFailureListener { e -> Log.w("remove plant fail", "Error deleting document", e) }
    }
}