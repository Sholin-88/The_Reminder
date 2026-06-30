package com.sholin.the_reminder.alarmManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.sholin.the_reminder.R
import com.sholin.the_reminder.RoomDB.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra("EXTRA_ID", 0)
        val header = intent.getStringExtra("EXTRA_HEADER") ?: "Reminder"
        val desc = intent.getStringExtra("EXTRA_DESC") ?: "Your alarm is ringing!"
        
        Log.d("AlarmReceiver", "Alarm received for ID=$id, Header=$header")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "alarm_channel_v2"
        
        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            val channel = NotificationChannel(
                channelId,
                "Alarm Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Reminder Alarms"
                setSound(soundUri, audioAttributes)
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle(header)
            .setContentText(desc)
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(id, notification)

        // Reschedule recurring alarm
        if (id != 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val goAsync = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = DatabaseProvider.getDatabase(context)
                    val dao = db.reminderDao()
                    val reminder = dao.getReminderById(id)
                    
                    if (reminder != null && reminder.alarm == true && reminder.repeatDays != null && reminder.repeatTime != null) {
                        val days = reminder.repeatDays.split(",").map { it.toInt() }
                        val time = LocalTime.parse(reminder.repeatTime)
                        
                        // Find the next occurrence that is at least 1 minute in the future
                        val nextTrigger = days.map { dayId ->
                            AlarmHelperImpl.calculateNextOccurrence(dayId, time)
                        }.filter { it > System.currentTimeMillis() + 30000 }.minOrNull() // 30 seconds buffer
                        
                        if (nextTrigger != null) {
                            dao.updateReminder(reminder.copy(date = nextTrigger.toString()))
                            
                            val scheduler = AlarmHelperImpl(context)
                            scheduler.setAlarm(
                                nextTrigger,
                                reminder.id,
                                reminder.header,
                                reminder.description
                            )
                            Log.d("AlarmReceiver", "Rescheduled alarm for ID=$id at $nextTrigger")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AlarmReceiver", "Error in rescheduling", e)
                } finally {
                    goAsync.finish()
                }
            }
        }
    }
}
