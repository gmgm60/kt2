package com.example.kt2.location.impl

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
import com.example.kt2.MainActivity
import com.example.kt2.R
import com.example.kt2.location.domain.LocationClint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class LocationService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClint: LocationClint

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClint = DefaultLocationClint(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
//        val snoozeIntent = Intent(this, MainActivity::class.java)
//        val snoozePendingIntent = PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)
//
//        // Create an explicit intent for an Activity in your app
        val showAppIntent = Intent(this, MainActivity::class.java).apply {
           // flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val showAppPendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, showAppIntent, PendingIntent.FLAG_IMMUTABLE)

        // Create an explicit intent for an Activity in your app
        val stopServiceIntent = Intent(this, LocationService::class.java).apply {
            action = ACTION_STOP
          //  startService(this)
        }
        val stopServicePendingIntent: PendingIntent =
            PendingIntent.getService(this, 0, stopServiceIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, Notification_Channel)
            .setContentTitle("Tracking Location...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .addAction(R.drawable.ic_launcher_foreground, "Show App", showAppPendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Stop Tracking", stopServicePendingIntent)

        startForeground(Notification_Id, notification.build())

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClint.getLocationFlow(1000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.altitude.toString().takeLast(3)
                val long = location.longitude.toString().takeLast(3)
                val updatedNotification = notification.setContentText(
                    "Location: ($lat,$long)"
                )
                notificationManager.notify(Notification_Id, updatedNotification.build())
            }.launchIn(serviceScope)


    }

    private fun stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        stopSelf()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val Notification_Id = 1
        const val Notification_Channel = "location"
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }
}