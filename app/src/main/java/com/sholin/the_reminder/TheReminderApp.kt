package com.sholin.the_reminder

import android.app.Application
import com.sholin.the_reminder.Repository.ReminderRepository
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
class TheReminderApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val entryPoint = EntryPointAccessors.fromApplication(this, ReminderSyncEntryPoint::class.java)
        entryPoint.reminderRepository().syncFromFirebase()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ReminderSyncEntryPoint {
    fun reminderRepository(): ReminderRepository
}
