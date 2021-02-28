package com.example.faceplant.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.faceplant.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val buttonSignUp = findViewById<Button>(R.id.sign_up_page_sign_up)
        val editTextSignIn = findViewById<TextView>(R.id.sign_in_text)
        var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val editTextEmail: EditText = findViewById(R.id.edittext_sign_up_email)
        val editTextPassword: EditText = findViewById(R.id.edittext_sign_up_password)
        val editTextConfirmPassword: EditText = findViewById(R.id.edittext_repeat_password)

        buttonSignUp.setOnClickListener {
            //Validate the entries
            when {
                TextUtils.isEmpty(editTextEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this, R.string.message_enter_email, Toast.LENGTH_SHORT
                    ).show()
                }

                !editTextEmail.text.toString().trim { it <= ' ' }
                    .matches(emailPattern.toRegex()) -> {
                    Toast.makeText(
                        this, R.string.message_enter_email, Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(editTextPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this, R.string.message_enter_password, Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(editTextConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this, R.string.message_confirm_password, Toast.LENGTH_SHORT
                    ).show()
                }

                editTextPassword.text.toString() != editTextConfirmPassword.text.toString() -> {
                    Toast.makeText(
                        this, R.string.message_password_mismatched, Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                    val email: String = editTextEmail.text.toString().trim { it <= ' ' }
                    val password: String = editTextPassword.text.toString().trim { it <= ' ' }

                    // Create an instance and create a register a user with email and password.
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

        }

        editTextSignIn.setOnClickListener {
            //Launch the SignInActivity when the sign_in_text clicked
            startActivity(
                Intent(this, SignInActivity::class.java)
            )
        }
        /*
        //Function to validate the entries
        private fun validateSignUpDetails() {
        }
         */
    }
}
