package com.sholin.the_reminder.alarmManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import com.sholin.the_reminder.domain.alarm.AlarmScheduler
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

class AlarmHelperImpl(private val context: Context) : AlarmScheduler {

    override fun setAlarm(triggerAtMillis: Long, id: Int, header: String, description: String) {
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_ID", id)
            putExtra("EXTRA_HEADER", header)
            putExtra("EXTRA_DESC", description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                try {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                    Log.d("AlarmHelper", "Alarm set for $header at $triggerAtMillis")
                } catch (e: SecurityException) {
                    Log.e("AlarmHelper", "SecurityException: Cannot set exact alarm", e)
                }
            } else {
                val intentSettings = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intentSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intentSettings)
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    override fun cancelAlarm(id: Int) {
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Log.d("AlarmHelper", "Alarm canceled for id=$id")
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun calculateNextOccurrence(dayId: Int, time: LocalTime): Long {
            val dayOfWeek = DayOfWeek.of(dayId)
            var date = LocalDate.now().with(TemporalAdjusters.nextOrSame(dayOfWeek))
            
            if (date == LocalDate.now() && time.isBefore(LocalTime.now())) {
                date = date.with(TemporalAdjusters.next(dayOfWeek))
            }
            
            return date.atTime(time).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }
    }
}
