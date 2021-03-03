package com.example.faceplant.activities

import android.annotation.SuppressLint
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val buttonSignUp = findViewById<Button>(R.id.sign_up_page_sign_up)
        val editTextSignIn = findViewById<TextView>(R.id.sign_in_text)
        var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val editTextEmail = findViewById<EditText>(R.id.edittext_sign_up_email)
        val editTextPassword = findViewById<EditText>(R.id.edittext_sign_up_password)
        val editTextConfirmPassword = findViewById<EditText>(R.id.edittext_repeat_password)

        //Function to validate the entries
        fun validateSignUpDetails(): Boolean{
            return when {
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

        //Function to create user with email and password
        fun signUpUser(){
            //Validate the entries
            if (validateSignUpDetails()) {

                val email: String = editTextEmail.text.toString().trim { it <= ' ' }
                val password: String = editTextPassword.text.toString().trim { it <= ' ' }

                // Create an instance and create a user with email and password.
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        OnCompleteListener<AuthResult> { task ->
                            // If the registration is successfully done
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                Toast.makeText(
                                    this,
                                    R.string.message_registered_successfully,
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this, HomeActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
                                intent.putExtra("email_id", email)
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

        //Create user account on clicking the SignUp button
        buttonSignUp.setOnClickListener {
                signUpUser()
        }

        //Send back user to SignInActivity
        editTextSignIn.setOnClickListener {
            onBackPressed()
        }
    }
}