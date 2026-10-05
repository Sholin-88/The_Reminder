package com.sholin.the_reminder.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sholin.the_reminder.Firebase.FirebaseProvider
import com.sholin.the_reminder.Firebase.FirebaseReminderDataSource
import com.sholin.the_reminder.RoomDB.DatabaseProvider
import kotlinx.coroutines.flow.first

class ReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val db = DatabaseProvider.getDatabase(applicationContext)
            val dao = db.reminderDao()
            val firebaseProvider = FirebaseProvider(applicationContext)
            val firebaseDataSource = FirebaseReminderDataSource(firebaseProvider)

            val reminders = dao.getAllUsers().first()
            reminders.forEach { reminder ->
                firebaseDataSource.saveReminder(reminder)
            }
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }
}
