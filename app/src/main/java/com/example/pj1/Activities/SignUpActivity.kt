package com.example.pj1.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pj1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignUpActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val signupEmail : EditText = findViewById(R.id.signup_username)
        val signupPassword : EditText = findViewById(R.id.signup_password)
        val signupButton : Button = findViewById(R.id.signup_button)
        val toSignin : TextView = findViewById(R.id.to_signin)

        signupButton.setOnClickListener {
            val username = signupEmail.text.toString().trim()
            val password = signupPassword.text.toString().trim()

            if (username.isEmpty()){
                signupEmail.error = "Email must not be empty"
            }
            else if (password.isEmpty()) {
                signupPassword.error = "Password must not be empty"
            }
            else {
                auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, SignInActivity::class.java))
                    }
                    else {
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            Toast.makeText(this, "Account already exists", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Sign up failed: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        toSignin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}