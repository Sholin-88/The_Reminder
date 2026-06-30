package com.sholin.the_reminder.domain.alarm

interface AlarmScheduler {
    fun setAlarm(triggerAtMillis: Long, id: Int, header: String, description: String)
    fun cancelAlarm(id: Int)
}
