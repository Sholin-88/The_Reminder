package com.sholin.the_reminder.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sholin.the_reminder.Firebase.FirebaseProvider
import com.sholin.the_reminder.domain.model.WeightRecord
import com.sholin.the_reminder.domain.use_case.CalculateIdealWeightUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class IdealWeightViewModel(
    application: Application,
    private val calculateIdealWeightUseCase: CalculateIdealWeightUseCase,
    private val firebase: FirebaseProvider
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
        val result = calculateIdealWeightUseCase(h, _gender.value)
        _idealWeight.value = result
        
        if (result.isNotEmpty()) {
            saveRecordToFirebase(h.toString(), _gender.value ?: "Unknown", result)
        }
    }

    private fun saveRecordToFirebase(height: String, gender: String, idealWeight: String) {
        val record = WeightRecord(height, gender, idealWeight)
        val databaseRef = firebase.getDatabaseReference("weight_records")
        databaseRef.push().setValue(record)
            .addOnSuccessListener {
                firebase.log("Weight record saved to Firebase")
            }
            .addOnFailureListener { e ->
                firebase.recordException(e)
            }
    }
}
