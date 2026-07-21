package com.sholin.the_reminder.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sholin.the_reminder.Firebase.FirebaseProvider
import com.sholin.the_reminder.Repository.ReminderRepository
import com.sholin.the_reminder.alarmManager.AlarmHelperImpl
import com.sholin.the_reminder.model.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    application: Application,
    private val repository: ReminderRepository,
    private val alarmHelper: AlarmHelperImpl,
    private val firebase: FirebaseProvider
) : AndroidViewModel(application) {
    val databaseRef = firebase.getDatabaseReference("/reminders/data")

    var header by mutableStateOf(TextFieldValue())
    var description by mutableStateOf(TextFieldValue())

    val selectedDayIds = mutableStateListOf<Int>()
    var selectedDaysTime by mutableStateOf<LocalTime?>(null)

    fun clearFields() {
        header = TextFieldValue("")
        description = TextFieldValue("")
        selectedDayIds.clear()
        selectedDaysTime = null
    }

    fun updateSelectedDaysTime(hour: Int, minute: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.selectedDaysTime = LocalTime.of(hour, minute)
        }
    }

    fun toggleDaySelection(dayId: Int) {
        if (selectedDayIds.contains(dayId)) {
            selectedDayIds.remove(dayId)
        } else {
            selectedDayIds.add(dayId)
        }
    }

    val isCloseVisible: Boolean
        get() = selectedDayIds.isNotEmpty() && description.text.isNotEmpty() && header.text.isNotEmpty()

    val isSaveEnabled: Boolean
        get() = (selectedDayIds.isNotEmpty() && selectedDaysTime != null) && description.text.isNotEmpty() && header.text.isNotEmpty()

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertData() {
        viewModelScope.launch {
            if (selectedDayIds.isNotEmpty() && selectedDaysTime != null) {
                firebase.log("Inserting reminder: ${header.text}")
                val daysString = selectedDayIds.joinToString(",")
                val timeString = selectedDaysTime.toString()

                val soonestTrigger = selectedDayIds
                    .map {
                        AlarmHelperImpl.Companion.calculateNextOccurrence(
                            it,
                            selectedDaysTime!!
                        )
                    }
                    .minOrNull() ?: 0L

                val reminder = Reminder(
                    header = header.text,
                    description = description.text,
                    date = soonestTrigger.toString(),
                    alarm = true,
                    repeatDays = daysString,
                    repeatTime = timeString
                )

                val id = repository.insertReminder(reminder)

                if (reminder.alarm == true) {
                    alarmHelper.setAlarm(
                        soonestTrigger,
                        id.toInt(),
                        reminder.header,
                        reminder.description
                    )
                }

                // Also save to Firebase for backup/sync
                saveReminderToFirebase(reminder.copy(id = id.toInt()))
            }
            clearFields()
        }
    }

    private fun saveReminderToFirebase(reminder: Reminder) {
        databaseRef.child(reminder.id.toString()).setValue(reminder)
            .addOnSuccessListener {
                firebase.log("Reminder saved to Firebase: ${reminder.id}")
            }
            .addOnFailureListener { e ->
                firebase.recordException(e)
            }
    }

    fun deleteData(id: Int) {
        viewModelScope.launch {
            firebase.log("Delete reminder: $id")
            alarmHelper.cancelAlarm(id)
            repository.deleteReminder(id)
        }
    }

    fun updateAlarm(reminder: Reminder, isEnabled: Boolean) {
        viewModelScope.launch {
            repository.updateAlarm(reminder.id, isEnabled)

            var nextTrigger: Long? = null
            if (isEnabled) {
                if (reminder.repeatDays != null && reminder.repeatTime != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val days = reminder.repeatDays.split(",").map { it.toInt() }
                    val time = LocalTime.parse(reminder.repeatTime)
                    nextTrigger =
                        days.map { AlarmHelperImpl.calculateNextOccurrence(it, time) }.minOrNull()
                } else {
                    nextTrigger = reminder.date.toLongOrNull()
                }

                if (nextTrigger != null) {
                    alarmHelper.setAlarm(
                        nextTrigger,
                        reminder.id,
                        reminder.header,
                        reminder.description
                    )
                }
            } else {
                alarmHelper.cancelAlarm(reminder.id)
            }
        }
    }

    val reminderList = repository.getReminderList()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
