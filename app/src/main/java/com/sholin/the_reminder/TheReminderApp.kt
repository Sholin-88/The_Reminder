package com.sholin.the_reminder

import android.app.Application
import com.sholin.the_reminder.Firebase.FirebaseProvider
import com.sholin.the_reminder.Repository.ReminderRepository
import com.sholin.the_reminder.RoomDB.DatabaseProvider
import com.sholin.the_reminder.alarmManager.AlarmHelperImpl

class TheReminderApp : Application() {
    
    val repository by lazy {
        ReminderRepository(DatabaseProvider.getDatabase(this).reminderDao())
    }

    val alarmHelper by lazy {
        AlarmHelperImpl(this)
    }

    val firebaseProvider by lazy {
        FirebaseProvider(this)
    }
}
