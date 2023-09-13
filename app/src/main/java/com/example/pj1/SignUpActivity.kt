package com.example.pj1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val auth = FirebaseAuth.getInstance()
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
                        Toast.makeText(this, "Sign up failed " + task.exception!! .message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        toSignin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}