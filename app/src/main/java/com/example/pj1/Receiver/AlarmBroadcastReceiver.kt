package com.example.pj1.Receiver

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pj1.Activities.OnAlarmActivity
import com.example.pj1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class AlarmBroadcastReceiver : BroadcastReceiver() {

    private val firebaseDataBase = FirebaseDatabase.getInstance("https://android-pj-70dfa-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    private val databaseReference = firebaseDataBase.getReference("Users")

    override fun onReceive(context: Context, intent: Intent) {
        val alarmID = intent.getStringExtra("ID")!!
        val alarmHour = intent.getIntExtra("Hour", -1)
        val alarmMin = intent.getIntExtra("Min", -1)
        val repeat = intent.getBooleanExtra("Repeat", false)
        val channelId = "alarm_channel"
        val channelName = "Alarm Notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val nextActivity = Intent(context, OnAlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("Hour", alarmHour)
            putExtra("Min", alarmMin)
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(context, 0, nextActivity, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = "Channel for alarm notifications"
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), null)
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_none_24)
            .setContentTitle("${alarmHour.toString().padStart(2, '0')}:${alarmMin.toString().padStart(2, '0')}")
            .setContentText("Time to wake up")
            .setAutoCancel(false)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(0)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))


        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(111, builder.build())
        }

        if (!repeat) {
            databaseReference.child(currentUserId).child("userListAlarm").child(alarmID).updateChildren(toMap())
        }
    }
    private fun toMap(): Map<String, Boolean> {
        val result: HashMap<String, Boolean> = HashMap()
        result["alarmState"] = false
        return  result
    }
}