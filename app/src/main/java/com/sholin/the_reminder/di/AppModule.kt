package com.sholin.the_reminder.di

import android.content.Context
import com.sholin.the_reminder.Firebase.FirebaseProvider
import com.sholin.the_reminder.Repository.ReminderRepository
import com.sholin.the_reminder.RoomDB.DatabaseProvider
import com.sholin.the_reminder.RoomDB.ReminderDao
import com.sholin.the_reminder.RoomDB.ReminderDataBase
import com.sholin.the_reminder.alarmManager.AlarmHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ReminderDataBase {
        return DatabaseProvider.getDatabase(context)
    }

    @Provides
    fun provideReminderDao(database: ReminderDataBase): ReminderDao {
        return database.reminderDao()
    }

    @Provides
    @Singleton
    fun provideReminderRepository(dao: ReminderDao): ReminderRepository {
        return ReminderRepository(dao)
    }

    @Provides
    @Singleton
    fun provideAlarmHelper(@ApplicationContext context: Context): AlarmHelperImpl {
        return AlarmHelperImpl(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseProvider(@ApplicationContext context: Context): FirebaseProvider {
        return FirebaseProvider(context)
    }
}
