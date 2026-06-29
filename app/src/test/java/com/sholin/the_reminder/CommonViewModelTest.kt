package com.sholin.the_reminder

import android.app.Application
import androidx.compose.ui.text.input.TextFieldValue
import com.sholin.the_reminder.domain.use_case.ReminderUseCases
import com.sholin.the_reminder.presentation.viewmodel.CommonViewModel
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalTime

class CommonViewModelTest {

    private lateinit var viewModel: CommonViewModel
    private val application = mockk<Application>(relaxed = true)
    private val useCases = mockk<ReminderUseCases>(relaxed = true)

    @Before
    fun setup() {
        viewModel = CommonViewModel(application, useCases)
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
    fun `isCloseVisible returns true when any field has content`() {
        // Initially false
        assertFalse(viewModel.isCloseVisible)

        // Add header
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
