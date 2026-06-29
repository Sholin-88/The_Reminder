package com.sholin.the_reminder

import android.app.Application
import com.sholin.the_reminder.RoomDB.DatabaseProvider
import com.sholin.the_reminder.data.repository.ReminderRepositoryImpl
import com.sholin.the_reminder.domain.use_case.*

class TheReminderApp : Application() {
    
    // Manual dependency injection for now, until Hilt is added
    val repository by lazy {
        ReminderRepositoryImpl(DatabaseProvider.getDatabase(this).reminderDao())
    }
    
    val reminderUseCases by lazy {
        ReminderUseCases(
            getReminders = GetRemindersUseCase(repository),
            addReminder = AddReminderUseCase(repository),
            deleteReminder = DeleteReminderUseCase(repository),
            updateAlarm = UpdateAlarmUseCase(repository),
            getReminderById = GetReminderByIdUseCase(repository)
        )
    }
    
    val calculateIdealWeightUseCase by lazy {
        CalculateIdealWeightUseCase()
    }
}
