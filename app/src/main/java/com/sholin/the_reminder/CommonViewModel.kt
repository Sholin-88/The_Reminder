package com.sholin.the_reminder

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.sholin.the_reminder.Repository.ReminderRepository
import com.sholin.the_reminder.RoomDB.DatabaseProvider
import com.sholin.the_reminder.Utils.formatMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Async

class CommonViewModel(application: Application) : AndroidViewModel(application) {
   val reminderRepository = ReminderRepository(application)

    var header by mutableStateOf(TextFieldValue())
    var description by mutableStateOf(TextFieldValue())
    var selectedDate by mutableStateOf(TextFieldValue(""))

    var selectedEpochTime by mutableStateOf(TextFieldValue(""))



    fun clearFields() {
        header = TextFieldValue("")
        description = TextFieldValue("")
        selectedDate = TextFieldValue("")
        selectedEpochTime = TextFieldValue("")
    }

    fun updateSelectedDate(selectedDate: Long?) {
        selectedDate?.let {
            this.selectedDate = TextFieldValue(formatMillis(it))
        }
    }

val isCloseVisible: Boolean
    get() = selectedDate.text.isNotEmpty() && description.text.isNotEmpty() && header.text.isNotEmpty() && selectedEpochTime.text.isNotEmpty()

    val isSaveEnabled: Boolean
    get() = selectedDate.text.isNotEmpty()  && description.text.isNotEmpty() && header.text.isNotEmpty() && selectedEpochTime.text.isNotEmpty()



    fun insertData() {
        viewModelScope.launch {
            reminderRepository.insertReminder(
                Reminder(
                    header = header.text, description = description.text, date = selectedDate.text, time = selectedEpochTime.text
                )
            )
            clearFields()
        }
    }

    fun deleteData(id: Int) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(id)

        }
    }

    fun setEpochTime(timeInMillis: Long) {
        selectedEpochTime = TextFieldValue(Utils.formatEpoch(timeInMillis))

    }

    fun updateAlarm(reminder: Reminder, it: Boolean) {
        viewModelScope.launch {
            reminderRepository.updateSwitchById(reminder.id,it)
        }
    }

    val reminderList =  reminderRepository.getReminderList().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())





}