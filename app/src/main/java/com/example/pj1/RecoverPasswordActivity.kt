package com.example.pj1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import com.google.firebase.auth.FirebaseAuth

class RecoverPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)

        val checkBox : CheckBox = findViewById(R.id.captcha_chkbx)
        val recoveryPassword : EditText = findViewById(R.id.recovery_password)
        val TAG = "MyActivity"
        val siteKey = "6LcTMw8oAAAAAK2yfssqc8E0nL4GCGptjEbcEPEV"
        val auth = FirebaseAuth.getInstance()
        val rstpasswordbtn : Button = findViewById(R.id.recovery_password_buton)

        rstpasswordbtn.setOnClickListener {
            val email: String = recoveryPassword.text.toString().trim()
            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (checkBox.isChecked) {
                    auth.sendPasswordResetEmail(email).addOnSuccessListener {
                        Toast.makeText(this, "Send email successful please check your email", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Send email failed: " + it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        checkBox.setOnClickListener {
            SafetyNet.getClient(this).verifyWithRecaptcha(siteKey)
                .addOnSuccessListener { response ->
                    val userResponseToken = response.tokenResult
                    if (response.tokenResult?.isNotEmpty() == true) {
                    }
                    finish()
                }.addOnFailureListener {e ->
                    if (e is ApiException) {
                        Log.d(TAG, "Error: ${CommonStatusCodes.getStatusCodeString(e.statusCode)}")
                    } else {
                        Log.d(TAG, "Error: ${e.message}")
                    }
                }
        }

    }
}