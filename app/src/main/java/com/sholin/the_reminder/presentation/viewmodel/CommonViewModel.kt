package com.sholin.the_reminder.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sholin.the_reminder.alarmManager.AlarmHelper
import com.sholin.the_reminder.domain.model.Reminder
import com.sholin.the_reminder.domain.use_case.ReminderUseCases
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime

class CommonViewModel(
    application: Application,
    private val useCases: ReminderUseCases
) : AndroidViewModel(application) {
    private val appContext: Context = application.applicationContext

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
                val daysString = selectedDayIds.joinToString(",")
                val timeString = selectedDaysTime.toString()

                val soonestTrigger = selectedDayIds
                    .map { AlarmHelper.calculateNextOccurrence(it, selectedDaysTime!!) }
                    .minOrNull() ?: 0L

                val reminder = Reminder(
                    header = header.text,
                    description = description.text,
                    date = soonestTrigger.toString(),
                    alarm = true,
                    repeatDays = daysString,
                    repeatTime = timeString
                )
                
                val id = useCases.addReminder(reminder).toInt()
                
                AlarmHelper.setAlarm(
                    appContext,
                    soonestTrigger,
                    id,
                    reminder.header,
                    reminder.description
                )
            }
            clearFields()
        }
    }

    fun deleteData(id: Int) {
        viewModelScope.launch {
            AlarmHelper.cancelAlarm(appContext, id)
            useCases.deleteReminder(id)
        }
    }

    fun updateAlarm(reminder: Reminder, isEnabled: Boolean) {
        viewModelScope.launch {
            useCases.updateAlarm(reminder.id, isEnabled)
            if (isEnabled) {
                if (reminder.repeatDays != null && reminder.repeatTime != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val days = reminder.repeatDays.split(",").map { it.toInt() }
                    val time = LocalTime.parse(reminder.repeatTime)
                    val nextTrigger = days.map { AlarmHelper.calculateNextOccurrence(it, time) }.minOrNull() ?: 0L
                    
                    AlarmHelper.setAlarm(
                        appContext,
                        nextTrigger,
                        reminder.id,
                        reminder.header,
                        reminder.description
                    )
                } else {
                    val triggerTime = reminder.date.toLongOrNull()
                    if (triggerTime != null) {
                        AlarmHelper.setAlarm(
                            appContext,
                            triggerTime,
                            reminder.id,
                            reminder.header,
                            reminder.description
                        )
                    }
                }
            } else {
                AlarmHelper.cancelAlarm(appContext, reminder.id)
            }
        }
    }

    val reminderList = useCases.getReminders()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
