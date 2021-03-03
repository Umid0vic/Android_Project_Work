package com.example.faceplant.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.faceplant.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val submitBtn = findViewById<Button>(R.id.submit_button)
        val emailEditText = findViewById<EditText>(R.id.forgot_password_emailEditText)

        submitBtn.setOnClickListener(){
            val email: String = emailEditText.text.toString().trim{ it <= ' '}

            if(email.isEmpty()){
                Toast.makeText(
                    this, R.string.message_enter_email, Toast.LENGTH_SHORT
                ).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {task ->
                        if(task.isSuccessful){
                            Toast.makeText(
                                this, R.string.message_check_your_email, Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }else{
                            Toast.makeText(
                                this, task.exception!!.message.toString(), Toast.LENGTH_SHORT
                            )
                        }
                    }
            }

        }
    }
}