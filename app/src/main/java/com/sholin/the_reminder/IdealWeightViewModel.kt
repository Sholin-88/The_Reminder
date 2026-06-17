package com.sholin.the_reminder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class IdealWeightViewModel(application: Application) : AndroidViewModel(application)  {

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
        if (h != null) {
            val inches = h / 2.54
            _idealWeight.value = if (_gender.value == "Male") {
                val devine = 50 + 2.3 * (inches - 60)
                "Ideal weight ≈ %.1f kg".format(devine)
            } else {
                val devine = 45.5 + 2.3 * (inches - 60)
                "Ideal weight ≈ %.1f kg".format(devine)
            }
        } else {
            _idealWeight.value = "Enter valid height"
        }
    }

}