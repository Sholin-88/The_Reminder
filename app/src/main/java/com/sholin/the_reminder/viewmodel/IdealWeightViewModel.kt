package com.sholin.the_reminder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sholin.the_reminder.Firebase.FirebaseProvider
import com.sholin.the_reminder.model.WeightRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class IdealWeightViewModel(
    application: Application,
    private val firebase: FirebaseProvider
) : AndroidViewModel(application)  {
    val databaseRef = firebase.getDatabaseReference("weight_records")
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
        val result = if (h == null) {
            "Enter valid height"
        } else {
            val inches = h / 2.54
            if (_gender.value == "Male") {
                val devine = 50 + 2.3 * (inches - 60)
                "Ideal weight ≈ %.1f kg".format(devine)
            } else {
                val devine = 45.5 + 2.3 * (inches - 60)
                "Ideal weight ≈ %.1f kg".format(devine)
            }
        }

        _idealWeight.value = result

        if (result.isNotEmpty() && h != null) {
            saveRecordToFirebase(h.toString(), _gender.value ?: "Unknown", result)
        }
    }

    private fun saveRecordToFirebase(height: String, gender: String, idealWeight: String) {
        val record = WeightRecord(height, gender, idealWeight)

        databaseRef.push().setValue(record)
            .addOnSuccessListener {
                firebase.log("Weight record saved to Firebase")
            }
            .addOnFailureListener { e ->
                firebase.recordException(e)
            }
    }
}