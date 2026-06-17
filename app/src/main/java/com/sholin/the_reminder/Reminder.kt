package com.sholin.the_reminder

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_reminder")
data class Reminder(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "header") val header: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "date") val date: String, // This will store the next occurrence millis
    @ColumnInfo(name = "alarm") val alarm: Boolean? = false,
    @ColumnInfo(name = "repeatDays") val repeatDays: String? = null, // Comma-separated day IDs (1-7)
    @ColumnInfo(name = "repeatTime") val repeatTime: String? = null // Format HH:mm
)
