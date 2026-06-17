package com.sholin.the_reminder.RoomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sholin.the_reminder.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM table_reminder WHERE id = :id")
    suspend fun getReminderById(id: Int): com.sholin.the_reminder.Reminder?

    @Insert
    suspend fun insertUser(reminder: Reminder): Long
    @Query("SELECT * FROM table_reminder ORDER BY id DESC")
     fun getAllUsers(): Flow<List<Reminder>>
    @Query("DELETE FROM table_reminder WHERE id = :id")
    suspend fun deleteUser(id: Int)

    @Update
    suspend fun updateReminder(reminder: Reminder)

    @Query("UPDATE table_reminder SET alarm = :alarm WHERE id = :id")
    suspend fun updateSwitchById(id: Int, alarm: Boolean)




}