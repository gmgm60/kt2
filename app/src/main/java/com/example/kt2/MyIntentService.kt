package com.example.kt2

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyIntentService : IntentService("MyService") {
    init {
        instance = this
    }

    companion object {
        private const val tag = "MyIntentService"
        private lateinit var instance: MyIntentService
        var isRunning = false
        fun stopService() {
            Log.d(tag, "service is stopping....")
            isRunning = false
            instance.stopSelf()
        }
    }

    override fun onHandleIntent(p0: Intent?) {
        try {
            isRunning = true
            while (isRunning) {
                Log.d(tag, "service is running....")
                Thread.sleep(1000)
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

}