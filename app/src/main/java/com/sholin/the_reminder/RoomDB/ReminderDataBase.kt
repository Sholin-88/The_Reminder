package com.sholin.the_reminder.RoomDB

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.sholin.the_reminder.model.Reminder

@Database(entities = [Reminder::class], version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ])
abstract class ReminderDataBase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}
