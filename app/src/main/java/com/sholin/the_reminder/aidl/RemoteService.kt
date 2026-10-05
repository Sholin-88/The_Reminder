package com.sholin.the_reminder.aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder

class RemoteService : Service() {
    private val binder = object : IRemoteService.Stub() {
        override fun addNumbers(a: Int, b: Int): Int = a + b
        override fun getMessage(): String = "Hello from App A!"
    }

    override fun onBind(intent: Intent?): IBinder = binder

}