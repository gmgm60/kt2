package com.example.kt2

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService: Service() {
    private val tag = "MyService"
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    private val thread = Thread{
        try{
            while (true) {
                Log.d(tag, "service is running....")
                Thread.sleep(1000)
            }
        }catch (e:InterruptedException){
            Log.d(tag,"thread.interrupt()")
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val dataString = intent?.getStringExtra("EXTRA_DATA")
        dataString?.let {
            Log.d(tag,dataString)
        }
        if(!thread.isAlive){
            thread.start()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        thread.interrupt()
        super.onDestroy()
    }
}