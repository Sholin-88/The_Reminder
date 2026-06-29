package com.sholin.the_reminder

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_reminder")
data class Reminder(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "header") val header: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "alarm") val alarm: Boolean? = false,
    @ColumnInfo(name = "repeatDays") val repeatDays: String? = null,
    @ColumnInfo(name = "repeatTime") val repeatTime: String? = null
)
