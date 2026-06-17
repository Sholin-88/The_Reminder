package com.sholin.the_reminder.RoomDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sholin.the_reminder.Reminder

@Database(entities = [Reminder::class], version = 2)
    abstract class ReminderDataBase : RoomDatabase() {
        abstract fun reminderDao(): ReminderDao
    }


