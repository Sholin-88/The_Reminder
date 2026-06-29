package com.sholin.the_reminder.domain.repositoryInterface

import com.sholin.the_reminder.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getReminderList(): Flow<List<Reminder>>
    suspend fun insertReminder(reminder: Reminder): Long
    suspend fun deleteReminder(id: Int)
    suspend fun updateAlarm(id: Int, isEnabled: Boolean)
    suspend fun getReminderById(id: Int): Reminder?
}
