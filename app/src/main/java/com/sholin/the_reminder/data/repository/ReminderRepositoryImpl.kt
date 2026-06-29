package com.sholin.the_reminder.data.repository

import com.sholin.the_reminder.RoomDB.ReminderDao
import com.sholin.the_reminder.data.mapper.toDomain
import com.sholin.the_reminder.data.mapper.toEntity
import com.sholin.the_reminder.domain.model.Reminder
import com.sholin.the_reminder.domain.repositoryInterface.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReminderRepositoryImpl(private val dao: ReminderDao) : ReminderRepository {
    override fun getReminderList(): Flow<List<Reminder>> {
        return dao.getAllUsers().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertReminder(reminder: Reminder): Long {
        return dao.insertUser(reminder.toEntity())
    }

    override suspend fun deleteReminder(id: Int) {
        dao.deleteUser(id)
    }

    override suspend fun updateAlarm(id: Int, isEnabled: Boolean) {
        dao.updateSwitchById(id, isEnabled)
    }

    override suspend fun getReminderById(id: Int): Reminder? {
        return dao.getReminderById(id)?.toDomain()
    }
}
