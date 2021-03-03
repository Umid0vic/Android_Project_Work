package com.example.faceplant.firestore
import android.util.Log
import com.example.faceplant.activities.SignUpActivity
import com.example.faceplant.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()
    //function to save users info in Firestore
    fun saveUserInfo(userInfo: User){
        //sets the date with userInfo under the collection named "users". Document id is users id.
        mFirestore.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
    }
}