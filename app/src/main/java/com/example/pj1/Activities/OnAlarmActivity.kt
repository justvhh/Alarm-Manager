package com.example.pj1.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pj1.databinding.ActivityOnAlarmBinding

class OnAlarmActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOnAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val stopButton = binding.stopButton
        val timeText = binding.timeText
        val alarmHour = intent?.getIntExtra("Hour", -1)
        val alarmMin = intent?.getIntExtra("Min", -1)

        timeText.text = alarmHour.toString().padStart(2,'0') + ":" + alarmMin.toString().padStart(2, '0')

        stopButton.setOnClickListener {
            finish()
        }
    }
}