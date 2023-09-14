package com.example.pj1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.botton_nav)
        val homeFragment = HomeFragment()
        val noficationFragment = NoficationFragment()
        val settingFragment = SettingFragment()
        supportFragmentManager.beginTransaction().replace(com.google.android.material.R.id.container, homeFragment).commit()

        bottomNavigationView.setOnItemReselectedListener {item ->
            when(item.itemId) {
                R.id.nav_home -> supportFragmentManager.beginTransaction().replace(com.google.android.material.R.id.container, homeFragment).commit()
                R.id.nav_notification -> supportFragmentManager.beginTransaction().replace(com.google.android.material.R.id.container, noficationFragment).commit()
                R.id.nav_setting -> supportFragmentManager.beginTransaction().replace(com.google.android.material.R.id.container, settingFragment).commit()
            }
        }
    }
}