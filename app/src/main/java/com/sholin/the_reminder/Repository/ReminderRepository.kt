package com.sholin.the_reminder.Repository

import com.sholin.the_reminder.Firebase.FirebaseProvider
import com.sholin.the_reminder.Firebase.FirebaseReminderDataSource
import com.sholin.the_reminder.RoomDB.ReminderDao
import com.sholin.the_reminder.model.Reminder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ReminderRepository(
    private val dao: ReminderDao,
    private val firebaseDataSource: FirebaseReminderDataSource,
    private val firebase: FirebaseProvider
) {
    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun getReminderList(): Flow<List<Reminder>> = dao.getAllUsers()

    suspend fun insertReminder(reminder: Reminder): Long {
        val id = dao.insertUser(reminder)
        syncToFirebase(reminder.copy(id = id.toInt()))
        return id
    }

    suspend fun deleteReminder(id: Int) {
        dao.deleteUser(id)
        syncScope.launch {
            runCatching { firebaseDataSource.deleteReminder(id) }
                .onFailure { firebase.recordException(it) }
        }
    }

    suspend fun updateAlarm(id: Int, isEnabled: Boolean) {
        dao.updateSwitchById(id, isEnabled)
        syncScope.launch {
            runCatching { firebaseDataSource.updateAlarm(id, isEnabled) }
                .onFailure { firebase.recordException(it) }
        }
    }

    suspend fun getReminderById(id: Int): Reminder? = dao.getReminderById(id)

    fun syncFromFirebase() {
        syncScope.launch {
            runCatching {
                val remoteReminders = firebaseDataSource.getAllReminders()
                remoteReminders.forEach { dao.insertOrReplace(it) }
                firebase.log("Synced ${remoteReminders.size} reminders from Firebase")
            }.onFailure { firebase.recordException(it) }
        }
    }

    private fun syncToFirebase(reminder: Reminder) {
        syncScope.launch {
            runCatching { firebaseDataSource.saveReminder(reminder) }
                .onSuccess { firebase.log("Reminder saved to Firebase: ${reminder.id}") }
                .onFailure { firebase.recordException(it) }
        }
    }
}
