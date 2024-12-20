package com.example.pj1.Activities

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.pj1.Entities.Alarm
import com.example.pj1.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CreateAlarmActivity : AppCompatActivity() {

    private val firebaseDataBase = FirebaseDatabase.getInstance("https://android-pj-70dfa-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    private val databaseReference = firebaseDataBase.getReference("Users")
    private val selectedDays = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_alarm)

        android.text.format.DateFormat.is24HourFormat(this)

        val timePicker: TimePicker = findViewById(R.id.setalarm_time)
        timePicker.setIs24HourView(true)

        val chipGroup: ChipGroup = findViewById(R.id.chip_group)
        val saveButton: TextView = findViewById<TextView>(R.id.save_alarm_button).apply {
            setOnClickListener { saveAlarm() }
        }


        chipGroup.setOnCheckedStateChangeListener { _, _ ->
            for (i in 0 until chipGroup.childCount) {
                val childView = chipGroup.getChildAt(i)
                if (childView is Chip) {
                    val chip: Chip = childView
                    val chipValue = chip.text.toString()
                    when {
                        chip.isChecked && !selectedDays.contains(chipValue) -> selectedDays.add(chipValue)
                        !chip.isChecked && selectedDays.contains(chipValue) -> selectedDays.remove(chipValue)
                    }
                }
            }
        }
    }

    private fun saveAlarm() {
        val alarmName: EditText = findViewById(R.id.setalarm_name)
        val timePicker: TimePicker = findViewById(R.id.setalarm_time)
        val hour = timePicker.hour
        val min = timePicker.minute
        val name = alarmName.text.toString()

        if (selectedDays.isEmpty()) {
            selectedDays.add(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        }
        importAlarmDataBase(Alarm(selectedDays, hour, min, name))
        selectedDays.clear()
        finish()
    }

    private fun importAlarmDataBase(alarm: Alarm) {
        val key = databaseReference.child(currentUserId).child("userListAlarm").push().key
        key?.let {
            alarm.alarmID = it
            databaseReference.child(currentUserId).child("userListAlarm").child(it).setValue(alarm)
        }
    }
}