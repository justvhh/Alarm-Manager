package com.example.pj1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val auth = FirebaseAuth.getInstance()
        val signinEmail : EditText = findViewById(R.id.signin_username)
        val signinPassword : EditText = findViewById(R.id.signin_password)
        val signinButton : Button = findViewById(R.id.signin_button)
        val toSignup : TextView = findViewById(R.id.to_signup)
        val rcvpassword: TextView = findViewById(R.id.to_recovery_password)

        signinButton.setOnClickListener {
            val username = signinEmail.text.toString().trim()
            val password = signinPassword.text.toString().trim()

            if (username.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                if (password.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(username, password).addOnSuccessListener {
                        Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Sign in failed: " + it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    signinPassword.error = "Password must not be empty"
                }
            }
            else {
                signinEmail.error = "Email must not be empty"
            }
        }

        toSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }


        rcvpassword.setOnClickListener {
            startActivity(Intent(this, RecoverPasswordActivity::class.java))
        }
    }


}