package com.sholin.the_reminder.Firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.sholin.the_reminder.model.Reminder
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseReminderDataSource(
    private val firebase: FirebaseProvider
) {
    private val remindersRef = firebase.getDatabaseReference(REMINDERS_PATH)

    suspend fun saveReminder(reminder: Reminder) {
        remindersRef.child(reminder.id.toString()).setValue(reminder).await()
    }

    suspend fun deleteReminder(id: Int) {
        remindersRef.child(id.toString()).removeValue().await()
    }

    suspend fun updateAlarm(id: Int, isEnabled: Boolean) {
        remindersRef.child(id.toString()).child("alarm").setValue(isEnabled).await()
    }

    suspend fun getAllReminders(): List<Reminder> = suspendCancellableCoroutine { continuation ->
        remindersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reminders = snapshot.children.mapNotNull { child ->
                    child.getValue(Reminder::class.java)
                }
                continuation.resume(reminders)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }

    companion object {
        const val REMINDERS_PATH = "reminders/data"
    }
}
