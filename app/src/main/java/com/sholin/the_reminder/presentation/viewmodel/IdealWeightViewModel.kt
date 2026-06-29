package com.sholin.the_reminder.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sholin.the_reminder.domain.use_case.CalculateIdealWeightUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class IdealWeightViewModel(
    application: Application,
    private val calculateIdealWeightUseCase: CalculateIdealWeightUseCase
) : AndroidViewModel(application)  {

    private val _height = MutableStateFlow("")
    val height: StateFlow<String> = _height

    private val _gender = MutableStateFlow<String?>("Male")
    val gender: StateFlow<String?> = _gender

    private val _idealWeight = MutableStateFlow("")
    val idealWeight: StateFlow<String> = _idealWeight


    fun setHeight(height: String) {
        _height.value = height
    }

    fun setGender(gender: String) {
        _gender.value = gender
    }

    fun calculateIdealWeight() {
        val h = _height.value.toDoubleOrNull()
        _idealWeight.value = calculateIdealWeightUseCase(h, _gender.value)
    }

}
