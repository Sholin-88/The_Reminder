package com.sholin.the_reminder.domain.use_case

import com.sholin.the_reminder.domain.model.Reminder
import com.sholin.the_reminder.domain.repositoryInterface.ReminderRepository
import kotlinx.coroutines.flow.Flow

class GetRemindersUseCase(private val repository: ReminderRepository) {
    operator fun invoke(): Flow<List<Reminder>> = repository.getReminderList()
}

class AddReminderUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(reminder: Reminder): Long = repository.insertReminder(reminder)
}

class DeleteReminderUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(id: Int) = repository.deleteReminder(id)
}

class UpdateAlarmUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(id: Int, isEnabled: Boolean) = repository.updateAlarm(id, isEnabled)
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
