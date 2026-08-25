package com.sholin.the_reminder

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.text.input.TextFieldValue
import com.sholin.the_reminder.Repository.ReminderRepository
import com.sholin.the_reminder.alarmManager.AlarmHelperImpl
import com.sholin.the_reminder.viewmodel.CommonViewModel
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalTime

class CommonViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CommonViewModel
    private val application = mockk<Application>(relaxed = true)
    private val repository = mockk<ReminderRepository>(relaxed = true)
    private val alarmHelper = mockk<AlarmHelperImpl>(relaxed = true)

    @Before
    fun setup() {
        viewModel = CommonViewModel(application, repository, alarmHelper)
    }

    @Test
    fun `isSaveEnabled returns false when fields are empty`() {
        viewModel.header = TextFieldValue("")
        viewModel.description = TextFieldValue("")
        viewModel.selectedDayIds.clear()
        viewModel.selectedDaysTime = null

        assertFalse(viewModel.isSaveEnabled)
    }

    @Test
    fun `isSaveEnabled returns true when all required fields are filled`() {
        viewModel.header = TextFieldValue("Test Header")
        viewModel.description = TextFieldValue("Test Description")
        viewModel.selectedDayIds.add(1) // Monday
        viewModel.selectedDaysTime = LocalTime.of(10, 0)

        assertTrue(viewModel.isSaveEnabled)
    }

    @Test
    fun `isSaveEnabled returns false when time is missing`() {
        viewModel.header = TextFieldValue("Test Header")
        viewModel.description = TextFieldValue("Test Description")
        viewModel.selectedDayIds.add(1)
        viewModel.selectedDaysTime = null

        assertFalse(viewModel.isSaveEnabled)
    }

    @Test
    fun `isCloseVisible returns true when all fields have content`() {
        // Initially false
        assertFalse(viewModel.isCloseVisible)

        // Add content to all required fields for isCloseVisible logic
        viewModel.header = TextFieldValue("H")
        viewModel.description = TextFieldValue("D")
        viewModel.selectedDayIds.add(1)
        
        assertTrue(viewModel.isCloseVisible)
    }

    @Test
    fun `clearFields resets all values`() {
        viewModel.header = TextFieldValue("Test")
        viewModel.description = TextFieldValue("Desc")
        viewModel.selectedDayIds.add(1)
        viewModel.selectedDaysTime = LocalTime.of(10, 0)

        viewModel.clearFields()

        assertEquals("", viewModel.header.text)
        assertEquals("", viewModel.description.text)
        assertTrue(viewModel.selectedDayIds.isEmpty())
        assertNull(viewModel.selectedDaysTime)
    }
}
