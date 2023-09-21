package com.example.pj1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.botton_nav)
        supportFragmentManager.beginTransaction().replace(com.google.android.material.R.id.container, HomeFragment()).commit()
        bottomNavigationView.setOnItemSelectedListener {item ->
            when(item.itemId) {
                R.id.nav_user -> supportFragmentManager.beginTransaction().replace(com.google.android.material.R.id.container, UserFragment()).commit()
                R.id.nav_home -> supportFragmentManager.beginTransaction().replace(com.google.android.material.R.id.container, HomeFragment()).commit()
                R.id.nav_notification -> supportFragmentManager.beginTransaction().replace(com.google.android.material.R.id.container, NotificationFragment()).commit()
                R.id.nav_alarm -> supportFragmentManager.beginTransaction().replace(com.google.android.material.R.id.container, AlarmFragment()).commit()
            }
            true
        }
    }
}