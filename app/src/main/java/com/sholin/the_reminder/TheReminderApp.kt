package com.sholin.the_reminder

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sholin.the_reminder.Repository.ReminderRepository
import com.sholin.the_reminder.workmanager.ReminderWorker
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class TheReminderApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val entryPoint = EntryPointAccessors.fromApplication(this, ReminderSyncEntryPoint::class.java)
        entryPoint.reminderRepository().syncFromFirebase()

        scheduleReminderSyncWork()
    }

    private fun scheduleReminderSyncWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<ReminderWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ReminderSyncWork",
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest
        )
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ReminderSyncEntryPoint {
    fun reminderRepository(): ReminderRepository
}
