package com.sholin.the_reminder

import android.app.Application
import com.sholin.the_reminder.domain.use_case.CalculateIdealWeightUseCase
import com.sholin.the_reminder.presentation.viewmodel.IdealWeightViewModel
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class IdealWeightViewModelTest {

    private lateinit var viewModel: IdealWeightViewModel
    private val application = mockk<Application>(relaxed = true)
    private val calculateIdealWeightUseCase = CalculateIdealWeightUseCase()

    @Before
    fun setup() {
        viewModel = IdealWeightViewModel(application, calculateIdealWeightUseCase)
    }

    @Test
    fun `calculateIdealWeight for Male returns correct value`() {
        // Given height 180cm and Male
        viewModel.setHeight("180")
        viewModel.setGender("Male")

        // When
        viewModel.calculateIdealWeight()

        // Then 180cm / 2.54 = 70.866 inches
        // Devine: 50 + 2.3 * (70.866 - 60) = 50 + 2.3 * 10.866 = 50 + 24.99 = 74.99
        assertEquals("Ideal weight ≈ 75.0 kg", viewModel.idealWeight.value)
    }

    @Test
    fun `calculateIdealWeight for Female returns correct value`() {
        // Given height 160cm and Female
        viewModel.setHeight("160")
        viewModel.setGender("Female")

        // When
        viewModel.calculateIdealWeight()

        // Then 160cm / 2.54 = 62.99 inches
        // Devine: 45.5 + 2.3 * (62.99 - 60) = 45.5 + 2.3 * 2.99 = 45.5 + 6.877 = 52.377
        assertEquals("Ideal weight ≈ 52.4 kg", viewModel.idealWeight.value)
    }

    @Test
    fun `calculateIdealWeight with invalid height returns error message`() {
        // Given invalid height
        viewModel.setHeight("abc")

        // When
        viewModel.calculateIdealWeight()

        // Then
        assertEquals("Enter valid height", viewModel.idealWeight.value)
    }
}
