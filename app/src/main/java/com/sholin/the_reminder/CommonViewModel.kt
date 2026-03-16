package com.sholin.the_reminder

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sholin.the_reminder.Repository.ReminderRepository
import com.sholin.the_reminder.RoomDB.DatabaseProvider
import com.sholin.the_reminder.Utils.formatMillis
import kotlinx.coroutines.launch

class CommonViewModel(application: Application) : AndroidViewModel(application) {
   val reminderRepository = ReminderRepository(application)

    var header by mutableStateOf(TextFieldValue())
    var description by mutableStateOf(TextFieldValue())
    var selectedDate by mutableStateOf(TextFieldValue("Please Select a date"))


    fun clearFields() {
        header = TextFieldValue("")
        description = TextFieldValue("")
        selectedDate = TextFieldValue("")
    }

    fun updateSelectedDate(selectedDate: Long?) {
        selectedDate?.let {
            this.selectedDate = TextFieldValue(formatMillis(it))
        }
    }

val isCloseVisible: Boolean
    get() = selectedDate.text.isNotEmpty() && description.text.isNotEmpty() && header.text.isNotEmpty()

    val isSaveEnabled: Boolean
    get() = selectedDate.text.isNotEmpty() && description.text.isNotEmpty() && header.text.isNotEmpty()



    fun insertData() {
        viewModelScope.launch {
            reminderRepository.insertReminder(
                Reminder(
                    header = header.text, description = description.text, date = selectedDate.text
                )
            )
        }
    }

}