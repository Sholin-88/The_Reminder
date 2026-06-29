package com.sholin.the_reminder

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sholin.the_reminder.RoomDB.ReminderDao
import com.sholin.the_reminder.RoomDB.ReminderDataBase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ReminderDatabaseTest {

    private lateinit var dao: ReminderDao
    private lateinit var db: ReminderDataBase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, ReminderDataBase::class.java).build()
        dao = db.reminderDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeReminderAndReadInList() = runBlocking {
        val reminder = Reminder(
            header = "Test Header",
            description = "Test Desc",
            date = "123456789",
            alarm = true
        )
        dao.insertUser(reminder)
        val allReminders = dao.getAllUsers().first()
        assertEquals(allReminders[0].header, "Test Header")
    }

    @Test
    fun deleteReminderWorks() = runBlocking {
        val reminder = Reminder(
            header = "To Delete",
            description = "Desc",
            date = "123",
            alarm = false
        )
        val id = dao.insertUser(reminder).toInt()
        dao.deleteUser(id)
        val allReminders = dao.getAllUsers().first()
        assertEquals(0, allReminders.size)
    }
}
