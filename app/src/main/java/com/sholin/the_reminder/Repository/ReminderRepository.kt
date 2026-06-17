package com.sholin.the_reminder.Repository

import android.app.Application
import com.sholin.the_reminder.Reminder
import com.sholin.the_reminder.RoomDB.DatabaseProvider

class ReminderRepository(val context: Application) {

    val database = DatabaseProvider.getDatabase(context)
    val dao = database.reminderDao()

     fun getReminderList() = dao.getAllUsers()


    suspend fun insertReminder(reminder: Reminder): Long = dao.insertUser(reminder)

    suspend fun deleteReminder(id: Int) = dao.deleteUser(id)

    suspend fun updateSwitchById(id: Int, alarm: Boolean) = dao.updateSwitchById(id, alarm)




}