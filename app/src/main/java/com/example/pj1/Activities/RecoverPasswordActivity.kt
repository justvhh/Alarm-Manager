package com.example.pj1.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pj1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class RecoverPasswordActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)

        val checkBox : CheckBox = findViewById(R.id.captcha_chkbx)
        val recoveryPassword : EditText = findViewById(R.id.recovery_password)
        val rstpasswordbtn : Button = findViewById(R.id.recovery_password_buton)
        val toSignin : TextView = findViewById(R.id.to_signin)

        rstpasswordbtn.setOnClickListener {
            val email: String = recoveryPassword.text.toString().trim()
            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (checkBox.isChecked) {
                    auth.sendPasswordResetEmail(email).addOnSuccessListener {
                        Toast.makeText(this, "Send email successful please check your email", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { exception ->
                        if (exception is FirebaseAuthInvalidUserException) {
                            Toast.makeText(this, "Email does not exist", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Send email failed: " + exception.message, Toast.LENGTH_SHORT).show()
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