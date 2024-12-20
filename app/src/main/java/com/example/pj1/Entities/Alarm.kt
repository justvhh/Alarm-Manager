package com.example.pj1.Entities

class Alarm(var alarmDays: List<String> = emptyList(), val alarmHour: Int = -1, val alarmMinute: Int = -1, val alarmName: String = "", val alarmState: Boolean = true, var alarmID: String = "") {
    fun toMap(state: Boolean): Map<String, Boolean> {
        val result: HashMap<String, Boolean> = HashMap()
        result["alarmState"] = state
        return  result
    }
}