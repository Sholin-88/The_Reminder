package com.sholin.the_reminder

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sholin.the_reminder.Repository.ReminderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CommonViewModel(application: Application) : AndroidViewModel(application) {
    val reminderRepository = ReminderRepository(application)

    var header by mutableStateOf(TextFieldValue())
    var description by mutableStateOf(TextFieldValue())
    var selectedDateEpoch by mutableStateOf(TextFieldValue(""))
    
    // Track selected day by ID (1 for Monday, 7 for Sunday)
    var selectedDayId by mutableIntStateOf(-1)

    fun clearFields() {
        header = TextFieldValue("")
        description = TextFieldValue("")
        selectedDateEpoch = TextFieldValue("")
        selectedDayId = -1
    }

    fun updateSelectedDate(selectedDate: Long?) {
        selectedDate?.let {
            this.selectedDateEpoch = TextFieldValue(it.toString())
        }
    }

    fun updateSelectedDay(dayId: Int) {
        this.selectedDayId = dayId
    }

    val isCloseVisible: Boolean
        get() = (selectedDateEpoch.text.isNotEmpty() || selectedDayId != -1) && description.text.isNotEmpty() && header.text.isNotEmpty()

    val isSaveEnabled: Boolean
        get() = (selectedDateEpoch.text.isNotEmpty() || selectedDayId != -1) && description.text.isNotEmpty() && header.text.isNotEmpty()

    fun insertData() {
        viewModelScope.launch {
            val dateValue = if (selectedDayId != -1) {
                getDayNameFromId(selectedDayId)
            } else {
                selectedDateEpoch.text
            }
            
            reminderRepository.insertReminder(
                Reminder(
                    header = header.text, 
                    description = description.text, 
                    date = selectedDateEpoch.text
                )
            )
            clearFields()
        }
    }

    private fun getDayNameFromId(id: Int): String {
        return when (id) {
            1 -> "Monday"
            2 -> "Tuesday"
            3 -> "Wednesday"
            4 -> "Thursday"
            5 -> "Friday"
            6 -> "Saturday"
            7 -> "Sunday"
            else -> ""
        }
    }

    fun deleteData(id: Int) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(id)
        }
    }

    fun updateAlarm(reminder: Reminder, it: Boolean) {
        viewModelScope.launch {
            reminderRepository.updateSwitchById(reminder.id, it)
        }
    }

    val reminderList = reminderRepository.getReminderList()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
