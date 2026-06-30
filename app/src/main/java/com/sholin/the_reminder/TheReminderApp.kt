package com.sholin.the_reminder

import android.app.Application
import com.sholin.the_reminder.RoomDB.DatabaseProvider
import com.sholin.the_reminder.alarmManager.AlarmHelperImpl
import com.sholin.the_reminder.data.repository.ReminderRepositoryImpl
import com.sholin.the_reminder.domain.use_case.*

class TheReminderApp : Application() {
    
    // Manual dependency injection for now, until Hilt is added
    val repository by lazy {
        ReminderRepositoryImpl(DatabaseProvider.getDatabase(this).reminderDao())
    }

    val alarmScheduler by lazy {
        AlarmHelperImpl(this)
    }
    
    val reminderUseCases by lazy {
        ReminderUseCases(
            getReminders = GetRemindersUseCase(repository),
            addReminder = AddReminderUseCase(repository, alarmScheduler),
            deleteReminder = DeleteReminderUseCase(repository, alarmScheduler),
            updateAlarm = UpdateAlarmUseCase(repository, alarmScheduler),
            getReminderById = GetReminderByIdUseCase(repository)
        )
    }
    
    val calculateIdealWeightUseCase by lazy {
        CalculateIdealWeightUseCase()
    }
}
