package com.sholin.the_reminder.domain.model

data class WeightRecord(
    val height: String,
    val gender: String,
    val idealWeight: String,
    val timestamp: Long = System.currentTimeMillis()
)
