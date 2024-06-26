package com.app.expired

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class Notifications  : Application(){
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            "channel_id",
            "Channel_name",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}