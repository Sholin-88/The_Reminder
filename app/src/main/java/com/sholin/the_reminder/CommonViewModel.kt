package com.sholin.the_reminder

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
import com.sholin.the_reminder.Repository.ReminderRepository
import com.sholin.the_reminder.alarmManager.AlarmHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

class CommonViewModel(application: Application) : AndroidViewModel(application) {
    val reminderRepository = ReminderRepository(application)
    private val appContext: Context = application.applicationContext

    var header by mutableStateOf(TextFieldValue())
    var description by mutableStateOf(TextFieldValue())
    var selectedDateEpoch by mutableStateOf(TextFieldValue(""))
    
    // Track multiple selected days by ID (1 for Monday, 7 for Sunday)
    val selectedDayIds = mutableStateListOf<Int>()
    
    // Dedicated time for multi-day reminders
    var selectedDaysTime by mutableStateOf<LocalTime?>(null)

    fun clearFields() {
        header = TextFieldValue("")
        description = TextFieldValue("")
        selectedDateEpoch = TextFieldValue("")
        selectedDayIds.clear()
        selectedDaysTime = null
    }

    fun updateSelectedDate(selectedDate: Long?) {
        selectedDate?.let {
            this.selectedDateEpoch = TextFieldValue(it.toString())
        }
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
        get() = (selectedDateEpoch.text.isNotEmpty() || selectedDayIds.isNotEmpty()) && description.text.isNotEmpty() && header.text.isNotEmpty()

    val isSaveEnabled: Boolean
        get() = (selectedDateEpoch.text.isNotEmpty() || (selectedDayIds.isNotEmpty() && selectedDaysTime != null)) && description.text.isNotEmpty() && header.text.isNotEmpty()

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertData() {
        viewModelScope.launch {
            if (selectedDayIds.isNotEmpty() && selectedDaysTime != null) {
                selectedDayIds.forEach { dayId ->
                    val triggerTime = calculateNextOccurrence(dayId, selectedDaysTime!!)
                    val reminder = Reminder(
                        header = header.text,
                        description = description.text,
                        date = triggerTime.toString(),
                        alarm = true
                    )
                    // insertReminder should return Long (the row ID)
                    reminderRepository.insertReminder(reminder)
                    
                    AlarmHelper.setAlarm(
                        appContext,
                        triggerTime,
                        reminder.id,
                        reminder.header,
                        reminder.description
                    )
                }
            } else if (selectedDateEpoch.text.isNotEmpty()) {
                val triggerTime = selectedDateEpoch.text.toLong()
                val reminder = Reminder(
                    header = header.text,
                    description = description.text,
                    date = triggerTime.toString(),
                    alarm = true
                )
                 reminderRepository.insertReminder(reminder)
                
                AlarmHelper.setAlarm(
                    appContext,
                    triggerTime,
                    reminder.id,
                    reminder.header,
                    reminder.description
                )
            }
            clearFields()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateNextOccurrence(dayId: Int, time: LocalTime): Long {
        val dayOfWeek = DayOfWeek.of(dayId)
        var date = LocalDate.now().with(TemporalAdjusters.nextOrSame(dayOfWeek))
        
        if (date == LocalDate.now() && time.isBefore(LocalTime.now())) {
            date = date.with(TemporalAdjusters.next(dayOfWeek))
        }
        
        return date.atTime(time).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun deleteData(id: Int) {
        viewModelScope.launch {
            AlarmHelper.cancelAlarm(appContext, id)
            reminderRepository.deleteReminder(id)
        }
    }

    fun updateAlarm(reminder: Reminder, isEnabled: Boolean) {
        viewModelScope.launch {
            reminderRepository.updateSwitchById(reminder.id, isEnabled)
            if (isEnabled) {
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
            } else {
                AlarmHelper.cancelAlarm(appContext, reminder.id)
            }
        }
    }

    val reminderList = reminderRepository.getReminderList()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
