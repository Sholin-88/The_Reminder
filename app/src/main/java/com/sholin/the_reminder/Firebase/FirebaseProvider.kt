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
