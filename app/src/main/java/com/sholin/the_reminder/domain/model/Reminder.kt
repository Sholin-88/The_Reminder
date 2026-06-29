package com.sholin.the_reminder.domain.model

data class Reminder(
    val id: Int = 0,
    val header: String,
    val description: String,
    val date: String,
    val alarm: Boolean? = false,
    val repeatDays: String? = null,
    val repeatTime: String? = null
)
