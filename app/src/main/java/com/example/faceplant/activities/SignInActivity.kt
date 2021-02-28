package com.example.faceplant.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.faceplant.R
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val signInButton = findViewById<Button>(R.id.sign_in_page_sign_in)
        val signUpText = findViewById<TextView>(R.id.sign_up_text) as TextView
        val editTextEmail: EditText = findViewById(R.id.edittext_sign_in_email)
        val editTextPassword: EditText = findViewById(R.id.edittext_sign_in_password)

        signInButton.setOnClickListener{
            when {
                TextUtils.isEmpty(editTextEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        R.string.message_enter_email,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(editTextPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        R.string.message_enter_password,
                        Toast.LENGTH_SHORT
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
                                    this,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }

        signUpText.setOnClickListener{
            //Launch the SignUpActivity when the sign_up_text clicked
            startActivity(
                Intent(this, SignUpActivity::class.java)
            )
        }
    }
}