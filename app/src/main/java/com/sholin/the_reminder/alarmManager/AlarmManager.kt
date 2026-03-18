package com.sholin.the_reminder.alarmManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log

object AlarmHelper {

    fun setAlarm(context: Context, triggerAtMillis: Long, requestCode: Int, header: String, description: String) {
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_ID", requestCode)
            putExtra("EXTRA_HEADER", header)
            putExtra("EXTRA_DESC", description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
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

    fun cancelAlarm(context: Context, requestCode: Int) {
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Log.d("AlarmHelper", "Alarm canceled for requestCode=$requestCode")
    }
}
