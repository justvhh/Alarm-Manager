package com.example.pj1.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pj1.Entities.Alarm
import com.example.pj1.Adapter.AlarmListAdapter
import com.example.pj1.Activities.CreateAlarmActivity
import com.example.pj1.databinding.FragmentAlarmBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

 class AlarmFragment : Fragment()  {
    private lateinit var binding: FragmentAlarmBinding

    private val firebaseDataBase = FirebaseDatabase.getInstance("https://android-pj-70dfa-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    private val databaseReference = firebaseDataBase.getReference("Users")
    private var alarmList = mutableListOf<Alarm>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)

        val createAlarmButton: ImageButton = binding.createAlarmButton

        createAlarmButton.setOnClickListener {
            startActivity(Intent(activity, CreateAlarmActivity::class.java))
        }

        val alarmListAdapter = AlarmListAdapter(requireContext(), alarmList)

        databaseReference.child(currentUserId).child("userListAlarm").addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val alarm: Alarm = snapshot.getValue(Alarm::class.java)!!
                alarmList.add(alarm)
                alarmList.sortWith(compareBy({ it.alarmHour }, { it.alarmMinute }))
                var position1 = -1
                val iterator: Iterator<Alarm> = alarmList.iterator()
                while (iterator.hasNext()) {
                    val a = iterator.next()
                    if (a.alarmID == alarm.alarmID) {
                        position1 = alarmList.indexOf(a)
                    }
                }
                alarmListAdapter.notifyItemInserted(position1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val alarm: Alarm = snapshot.getValue(Alarm::class.java)!!
                if (alarmList.isEmpty()) {
                    return
                }
                var position = -1
                val iterator: Iterator<Alarm> = alarmList.iterator()
                while (iterator.hasNext()) {
                    val a = iterator.next()
                    if (a.alarmID == alarm.alarmID) {
                        position = alarmList.indexOf(a)
                        alarmList[position] = alarm
                    }
                }
                alarmList.sortWith(compareBy({ it.alarmHour }, { it.alarmMinute }))
                alarmListAdapter.notifyItemChanged(position)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val alarm: Alarm = snapshot.getValue(Alarm::class.java)!!
                if (alarmList.isEmpty()) {
                    return
                }
                var position: Int = -1
                val removeList = mutableListOf<Alarm>()
                for (a in alarmList) {
                    if (a.alarmID == alarm.alarmID) {
                        position = alarmList.indexOf(a)
                        removeList.add(a)
                    }
                }
                alarmList.removeAll(removeList)
                alarmListAdapter.notifyItemRemoved(position)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val recyclerView: RecyclerView = binding.recyclerView

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = alarmListAdapter


        return binding.root
    }

 }