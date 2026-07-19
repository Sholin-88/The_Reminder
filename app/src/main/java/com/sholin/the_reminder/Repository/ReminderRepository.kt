package com.sholin.the_reminder.Repository

import com.sholin.the_reminder.RoomDB.ReminderDao
import com.sholin.the_reminder.model.Reminder
import kotlinx.coroutines.flow.Flow

class ReminderRepository(private val dao: ReminderDao) {
    fun getReminderList(): Flow<List<Reminder>> = dao.getAllUsers()

    suspend fun insertReminder(reminder: Reminder): Long = dao.insertUser(reminder)

    suspend fun deleteReminder(id: Int) = dao.deleteUser(id)

    suspend fun updateAlarm(id: Int, isEnabled: Boolean) = dao.updateSwitchById(id, isEnabled)

    suspend fun getReminderById(id: Int): Reminder? = dao.getReminderById(id)
}