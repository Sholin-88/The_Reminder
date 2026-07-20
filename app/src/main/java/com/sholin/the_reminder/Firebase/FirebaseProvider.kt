package com.sholin.the_reminder.Firebase

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase

class FirebaseProvider(context: Context) {
    private val crashlytics = FirebaseCrashlytics.getInstance()
    private val analytics = FirebaseAnalytics.getInstance(context)
    private val database = FirebaseDatabase.getInstance()

    init {
        // Test connection
        database.getReference(".info/connected").addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    android.util.Log.d("FirebaseCheck", "Realtime Database connected")
                } else {
                    android.util.Log.d("FirebaseCheck", "Realtime Database disconnected")
                }
            }
            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                android.util.Log.e("FirebaseCheck", "Database connection error: ${error.message}")
            }
        })
    }

    fun getDatabaseReference(path: String) = database.getReference(path)

    fun log(message: String) {
        crashlytics.log(message)
    }

    fun recordException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    fun logEvent(eventName: String, params: Bundle? = null) {
        analytics.logEvent(eventName, params)
    }

    fun setUserId(userId: String) {
        crashlytics.setUserId(userId)
        analytics.setUserId(userId)
    }

    fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }
}
