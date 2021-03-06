package com.example.faceplant.firestore
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.faceplant.activities.SignInActivity
import com.example.faceplant.activities.SignUpActivity
import com.example.faceplant.models.User
import com.example.faceplant.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    //Function to save user info in Firestore
    fun saveUserInfo(userInfo: User){
        //Sets the data with userInfo under the collection named "users". Document id is users id.
        mFirestore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
    }

    //Function to get user info from Firestore
    fun getUserInfo(activity: Activity){
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null){
            currentUserId = currentUser.uid
        }

        mFirestore.collection(Constants.USERS)
            .document(currentUserId)
            .get()
            .addOnSuccessListener { document->
                // Converting document snapshot to User data model object
                val user = document.toObject(User::class.java)

                val sharedPreferences = activity.getSharedPreferences(Constants.FACEPLANT_PREFERENCES, Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                // Saving username inside the sharedPreferences key: UsernamePrefKey
                editor.putString(Constants.USERNAME_PREF_KEY, user?.username)
                editor.apply()

                when(activity){
                    is SignInActivity->{
                        if (user != null) {
                            activity.userSignedInSuccess(user)
                        }
                    }
                }
        }
    }
}