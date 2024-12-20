package com.example.pj1

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.pj1.Entities.Alarm
import com.example.pj1.Fragments.AlarmFragment
import com.example.pj1.Fragments.HomeFragment
import com.example.pj1.Fragments.NotificationFragment
import com.example.pj1.Fragments.UserFragment
import com.example.pj1.Receiver.AlarmBroadcastReceiver
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private val firebaseDataBase = FirebaseDatabase.getInstance("https://android-pj-70dfa-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    private val databaseReference = firebaseDataBase.getReference("Users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val channel = NotificationChannel(
            "111",
            "High priority notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

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

        databaseReference.child(currentUserId).child("userListAlarm").addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val alarm: Alarm = snapshot.getValue(Alarm::class.java)!!
                showNotification(alarm)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val alarm: Alarm = snapshot.getValue(Alarm::class.java)!!

                val requestCode = alarm.alarmHour * 10.0.pow(2 + alarm.alarmDays.size) + alarm.alarmMinute * 10.0.pow(alarm.alarmDays.size) + when(alarm.alarmDays[0]) {
                    "Mon" -> 2
                    "Tue" -> 3
                    "Wed" -> 4
                    "Thu" -> 5
                    "Fri" -> 6
                    "Sat" -> 7
                    "Sun" -> 8
                    else -> 1
                }
                alarmManager.cancel(PendingIntent.getBroadcast(this@MainActivity, requestCode.toInt(), intent, PendingIntent.FLAG_IMMUTABLE))
                showNotification(alarm)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val alarm: Alarm = snapshot.getValue(Alarm::class.java)!!
                val requestCode = alarm.alarmHour * 10.0.pow(2 + alarm.alarmDays.size) + alarm.alarmMinute * 10.0.pow(alarm.alarmDays.size) + when(alarm.alarmDays[0]) {
                    "Mon" -> 2
                    "Tue" -> 3
                    "Wed" -> 4
                    "Thu" -> 5
                    "Fri" -> 6
                    "Sat" -> 7
                    "Sun" -> 8
                    else -> 1
                }
                Log.i("Remove", alarm.alarmID)
                alarmManager.cancel(PendingIntent.getBroadcast(this@MainActivity, requestCode.toInt(), Intent(this@MainActivity, AlarmBroadcastReceiver::class.java).apply {
                    putExtra("ID", alarm.alarmID).putExtra("Hour", alarm.alarmHour).putExtra("Min", alarm.alarmMinute)
                }, PendingIntent.FLAG_IMMUTABLE))
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun showNotification(alarm: Alarm) {
        val calendar = Calendar.getInstance()

        for (i in alarm.alarmDays) {
            var repeat = false
            calendar.set(Calendar.HOUR_OF_DAY, alarm.alarmHour)
            calendar.set(Calendar.MINUTE, alarm.alarmMinute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            if (!i.contains("/")) {
                repeat = true
                calendar.set(Calendar.DAY_OF_WEEK, when(i) {
                    "Mon" -> Calendar.MONDAY
                    "Tue" -> Calendar.TUESDAY
                    "Wed" -> Calendar.WEDNESDAY
                    "Thu" -> Calendar.THURSDAY
                    "Fri" -> Calendar.FRIDAY
                    "Sat" -> Calendar.SATURDAY
                    else -> Calendar.SUNDAY
                })
            }

            if (Calendar.getInstance().before(calendar) && alarm.alarmState) {
                val intent = Intent(this, AlarmBroadcastReceiver::class.java).apply {
                    putExtra("ID", alarm.alarmID).putExtra("Hour", alarm.alarmHour).putExtra("Min", alarm.alarmMinute).putExtra("Repeat", repeat)
                }
                val requestCode = alarm.alarmHour * 1000 + alarm.alarmMinute * 10 + when(i) {
                    "Mon" -> 2
                    "Tue" -> 3
                    "Wed" -> 4
                    "Thu" -> 5
                    "Fri" -> 6
                    "Sat" -> 7
                    "Sun" -> 8
                    else -> 1
                }
                val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                when {
                    alarmManager.canScheduleExactAlarms() -> {
                        if (repeat) {
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,AlarmManager.INTERVAL_DAY * 7, pendingIntent)
                        }
                        else {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                        }
                    }
                    else -> {
                        startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                    }
                }
            }

        }


    }
}