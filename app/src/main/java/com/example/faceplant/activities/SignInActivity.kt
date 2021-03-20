package com.example.faceplant.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.faceplant.R
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.User
import com.example.faceplant.utils.Constants
import com.example.faceplant.utils.SharedPrefsClass
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_profile.*

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var forgotPasswordTextView: TextView
    private val db = FirebaseFirestore.getInstance()
    private var userImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val signInButton = findViewById<Button>(R.id.sign_in_page_sign_in)
        val continueWithGoogle = findViewById<Button>(R.id.continue_with_google)
        val signUpText = findViewById<TextView>(R.id.sign_up_text)
        emailEditText = findViewById(R.id.view_plant_dateEditText)
        passwordEditText = findViewById(R.id.add_plant_plantHealthEditText)
        forgotPasswordTextView = findViewById(R.id.forgot_password_textView)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) //string that Firebase automatically added
            .requestEmail()
            .build()
        // Initialize googleSignInClient
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        //Prompt the user to sign in with Google account
        continueWithGoogle.setOnClickListener(){
            signInWithGoogle()
        }

        signInButton.setOnClickListener{
            signInUser()
        }

        forgotPasswordTextView.setOnClickListener(){
            //Launch the ForgotPasswordActivity when forgotpassword? clicked
            startActivity(
                Intent(this, ForgotPasswordActivity::class.java)
            )
        }

        signUpText.setOnClickListener{
            //Launch the SignUpActivity when the sign_up_text clicked
            startActivity(
                Intent(this, SignUpActivity::class.java)
            )
        }
    }
    //Function to validate user inputs and sign in the user
    private fun signInUser(){
        when {
            TextUtils.isEmpty(emailEditText.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this, R.string.message_enter_email, Toast.LENGTH_SHORT
                ).show()
            }

            TextUtils.isEmpty(passwordEditText.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this, R.string.message_enter_password, Toast.LENGTH_SHORT
                ).show()
            }
            else -> {

                val email: String = emailEditText.text.toString().trim { it <= ' ' }
                val password: String = passwordEditText.text.toString().trim { it <= ' ' }

                // SignIn using FirebaseAuth
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {

                            val userId = task.result!!.user.uid
                            if (userId != null) {
                                db.collection(Constants.USERS).document(userId).get()
                                    .addOnSuccessListener { document ->
                                        // Converting document snapshot to User data model object
                                        val user = document.toObject(User::class.java)!!
                                        // Storing username, email and user image in SharedPreferences
                                        SharedPrefsClass().setSharedPreference(
                                            this, Constants.USER_PREFS, Constants.USERNAME_PREF_KEY, user.username
                                        )
                                        SharedPrefsClass().setSharedPreference(
                                            this, Constants.USER_PREFS, Constants.EMAIL_PREF_KEY, user.email
                                        )
                                        SharedPrefsClass().setSharedPreference(
                                            this, Constants.USER_PREFS, Constants.PROFILE_IMAGE_PREF_KEY, user.image
                                        )
                                        Toast.makeText(this,"User Data Stored",Toast.LENGTH_SHORT).show()
                                    }
                            }
                                  startActivity(Intent(this, UserProfileActivity::class.java))
                                  finish()
                        } else {
                            // If the login is not successful then show error message.
                            Toast.makeText(
                                this, task.exception!!.message.toString(), Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    fun userSignInSuccess(user: User){
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra(Constants.USER_DETAILS, user)
        startActivity(intent)
        finish()
    }
    //The code is from Firebase guides
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    //Check if user has successfully signed in. The code is from Firebase guides
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(SIGN_IN_ACTIVITY, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(SIGN_IN_ACTIVITY, "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, go to HomeActivity
                    Log.d(SIGN_IN_ACTIVITY, "signInWithCredential:success")
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    //create User object
                    val username = firebaseUser.displayName
                    val email = firebaseUser.email
                    val user = User(
                        firebaseUser.uid,
                        email,
                        username)
                    //create user document with user info in the Cloud Firestore
                    FirestoreClass().registerUserDetails(this, user)

                    // Storing username and email in SharedPreferences
                    SharedPrefsClass().setSharedPreference(
                        this, Constants.USER_PREFS, Constants.USERNAME_PREF_KEY, username
                    )

                    SharedPrefsClass().setSharedPreference(
                        this, Constants.USER_PREFS, Constants.EMAIL_PREF_KEY, email
                    )

                    Toast.makeText(this,"Data Stored",Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, UserProfileActivity::class.java)
                    intent.putExtra(Constants.USER_DETAILS, user)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(SIGN_IN_ACTIVITY, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this, R.string.message_registation_failed, Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    companion object{
        private const val RC_SIGN_IN = 100 //you can give any number
        const val SIGN_IN_ACTIVITY = "SIGN_IN_ACTIVITY"

    }
}