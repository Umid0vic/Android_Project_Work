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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val buttonSignUp = findViewById<Button>(R.id.sign_up_page_sign_up)
        val textViewSignIn = findViewById<TextView>(R.id.sign_in_text)
        editTextUsername = findViewById(R.id.sign_up_editTextUsername)
        editTextEmail = findViewById(R.id.sign_in_editTextEmail)
        editTextPassword = findViewById(R.id.sign_in_editTextPassword)
        editTextConfirmPassword = findViewById(R.id.sign_up_editTextRepeatPassword)


        //Create user account on clicking the SignUp button
        buttonSignUp.setOnClickListener {
                signUpUser()
        }

        //Send back user to SignInActivity
        textViewSignIn.setOnClickListener {
            onBackPressed()
        }
    }

    //Function to create user with email and password
    private fun signUpUser(){
        //Validate the entries
        if (validateSignUpDetails()) {

            val username: String = editTextUsername.text.toString().trim{ it <= ' '}
            val email: String = editTextEmail.text.toString().trim { it <= ' ' }
            val password: String = editTextPassword.text.toString().trim { it <= ' ' }

            // Create an instance and create a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        // If the registration is successfully done
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            //create User object
                            val user = User(
                                firebaseUser.uid,
                                email,
                                username
                            )
                            FirestoreClass().saveUserInfo(user)

                            Toast.makeText(
                                this,
                                R.string.message_registered_successfully,
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this, UserProfileActivity::class.java)
                            intent.putExtra(Constants.USER_DETAILS, user)
                            startActivity(intent)
                            finish()
                        } else {
                            // If the registering is not successful then show error message.
                            Toast.makeText(
                                this,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }

    //Function to validate the user entries
    private fun validateSignUpDetails(): Boolean{
        return when {
            TextUtils.isEmpty(editTextUsername.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this, R.string.message_enter_username, Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(editTextEmail.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this, R.string.message_enter_email, Toast.LENGTH_SHORT
                ).show()
                false
            }

            !editTextEmail.text.toString().trim { it <= ' ' }
                .matches(emailPattern.toRegex()) -> {
                Toast.makeText(
                    this, R.string.message_enter_email, Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(editTextPassword.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this, R.string.message_enter_password, Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(editTextConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this, R.string.message_confirm_password, Toast.LENGTH_SHORT
                ).show()
                false
            }

            editTextPassword.text.toString() != editTextConfirmPassword.text.toString() -> {
                Toast.makeText(
                    this, R.string.message_password_mismatched, Toast.LENGTH_SHORT
                ).show()
                false
            }

            else -> {
                Log.w("SignUpActivity", "User successfully signed up")
                true
            }
        }
    }
}