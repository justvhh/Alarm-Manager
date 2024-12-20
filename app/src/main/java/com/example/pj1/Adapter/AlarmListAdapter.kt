package com.example.pj1.Adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pj1.Entities.Alarm
import com.example.pj1.R
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AlarmListAdapter(val context: Context, private val alarmList: MutableList<Alarm>): RecyclerView.Adapter<AlarmListAdapter.ViewHolder>() {

    private val firebaseDataBase = FirebaseDatabase.getInstance("https://android-pj-70dfa-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    private val databaseReference = firebaseDataBase.getReference("Users")



    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val alarmName: TextView = view.findViewById(R.id.alarm_name)
        val alarmTime: TextView = view.findViewById(R.id.alarm_time)
        val alarmDays: TextView = view.findViewById(R.id.alarm_days)
        val chip: Chip = view.findViewById(R.id.on_off)
        val layout1: LinearLayout = view.findViewById(R.id.relative_layout1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_alarm, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = alarmList[position]
        val alarmId = alarm.alarmID
        holder.alarmName.text = alarm.alarmName
        holder.alarmTime.text = alarm.alarmHour.toString().padStart(2,'0') + ":" + alarm.alarmMinute.toString().padStart(2, '0')
        holder.alarmDays.text = alarm.alarmDays.toString()
        holder.chip.isChecked = alarm.alarmState
        holder.chip.text = when (holder.chip.isChecked) {
            true -> "on"
            false -> "off"
        }
        holder.chip.setOnClickListener {
            if (holder.chip.isChecked) {
                holder.chip.text = "on"
                databaseReference.child(currentUserId).child("userListAlarm").child(alarmId).updateChildren(
                    alarm.toMap(true)
                )
            } else {
                holder.chip.text = "off"
                databaseReference.child(currentUserId).child("userListAlarm").child(alarmId).updateChildren(
                    alarm.toMap(false)
                )
            }
        }


        holder.layout1.setOnLongClickListener {
            val linearLayout = LinearLayout(context)
            linearLayout.setBackgroundResource(R.color.red)
            linearLayout.gravity = Gravity.CENTER
            val params1 = LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT)
            params1.weight = 1f
            linearLayout.layoutParams = params1
            val imageView = ImageView(context)
            imageView.setImageResource(R.drawable.baseline_alarm_24)
            val params2 = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            imageView.layoutParams = params2
            linearLayout.addView(imageView)
            holder.layout1.addView(linearLayout)
            linearLayout.setOnClickListener {
                databaseReference.child(currentUserId).child("userListAlarm").child(alarmId).removeValue()
                holder.layout1.removeView(linearLayout)
            }
            false
        }

    }

}