package com.sholin.the_reminder.domain.use_case

import com.sholin.the_reminder.domain.alarm.AlarmScheduler
import com.sholin.the_reminder.domain.model.Reminder
import com.sholin.the_reminder.domain.repositoryInterface.ReminderRepository
import kotlinx.coroutines.flow.Flow

class GetRemindersUseCase(private val repository: ReminderRepository) {
    operator fun invoke(): Flow<List<Reminder>> = repository.getReminderList()
}

class AddReminderUseCase(
    private val repository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(reminder: Reminder, triggerAt: Long? = null): Long {
        val id = repository.insertReminder(reminder)
        if (reminder.alarm == true && triggerAt != null) {
            alarmScheduler.setAlarm(triggerAt, id.toInt(), reminder.header, reminder.description)
        }
        return id
    }
}

class DeleteReminderUseCase(
    private val repository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(id: Int) {
        alarmScheduler.cancelAlarm(id)
        repository.deleteReminder(id)
    }
}

class UpdateAlarmUseCase(
    private val repository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(reminder: Reminder, isEnabled: Boolean, triggerAt: Long? = null) {
        repository.updateAlarm(reminder.id, isEnabled)
        if (isEnabled && triggerAt != null) {
            alarmScheduler.setAlarm(triggerAt, reminder.id, reminder.header, reminder.description)
        } else {
            alarmScheduler.cancelAlarm(reminder.id)
        }
    }
}

class GetReminderByIdUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(id: Int): Reminder? = repository.getReminderById(id)
}

data class ReminderUseCases(
    val getReminders: GetRemindersUseCase,
    val addReminder: AddReminderUseCase,
    val deleteReminder: DeleteReminderUseCase,
    val updateAlarm: UpdateAlarmUseCase,
    val getReminderById: GetReminderByIdUseCase
)
