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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) //string that Firebase automatically added
            .requestEmail()
            .build()
        // Initialize googleSignInClient
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val signInButton = findViewById<Button>(R.id.sign_in_page_sign_in)
        val continueWithGoogle = findViewById<Button>(R.id.continue_with_google)
        val signUpText = findViewById<TextView>(R.id.sign_up_text)
        val editTextEmail = findViewById<EditText>(R.id.edittext_sign_in_email)
        val editTextPassword = findViewById<EditText>(R.id.edittext_sign_in_password)
        val textViewForgotPassword = findViewById<TextView>(R.id.forgot_password_textView)

        //Prompt the user to sign in with Google account
        continueWithGoogle.setOnClickListener(){
            signInWithGoogle()
        }


        signInButton.setOnClickListener{
            when {
                TextUtils.isEmpty(editTextEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this, R.string.message_enter_email, Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(editTextPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this, R.string.message_enter_password, Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                    val email: String = editTextEmail.text.toString().trim { it <= ' ' }
                    val password: String = editTextPassword.text.toString().trim { it <= ' ' }

                    // SignIn using FirebaseAuth
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                val intent =
                                    Intent(this, HomeActivity::class.java)
                         //       intent.flags =
                         //           Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
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

        textViewForgotPassword.setOnClickListener(){
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
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(SIGN_IN_ACTIVITY, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this, R.string.message_registered_successfully, Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    companion object{
        private const val RC_SIGN_IN = 100 //you can give any number
        const val SIGN_IN_ACTIVITY = "SIGN_IN_ACTIVITY"

    }
}