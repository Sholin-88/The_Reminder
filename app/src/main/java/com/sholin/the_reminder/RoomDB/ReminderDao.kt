package com.sholin.the_reminder.RoomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sholin.the_reminder.Reminder
@Dao
interface ReminderDao {
    @Insert
    suspend fun insertUser(reminder: Reminder)

    @Query("SELECT * FROM table_reminder")
    suspend fun getAllUsers(): List<Reminder>

    @Query("DELETE FROM table_reminder WHERE id = :id")
    suspend fun deleteUser(id: Int)

}