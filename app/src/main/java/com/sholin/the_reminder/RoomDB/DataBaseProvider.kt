package com.sholin.the_reminder.RoomDB
import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var db: ReminderDataBase? = null
    fun getDatabase(context: Context): ReminderDataBase {
        if (db == null) {
            db = Room.databaseBuilder(
                context.applicationContext,
                ReminderDataBase::class.java,
                "reminder_database"
            ).fallbackToDestructiveMigration()
                .build()
        }
        return db!!
    }
}