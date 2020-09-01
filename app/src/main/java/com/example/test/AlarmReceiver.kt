package com.example.test

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

class AlarmReceiver : BroadcastReceiver() {
    val ID = "CHANEL_ID"
    val channelName = "CHANNEL_NAME"

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationId: Int = intent?.getIntExtra("notification_id",0) ?: 0
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelNotification: NotificationCompat.Builder =
            NotificationCompat.Builder(context, ID).setContentTitle(intent?.getStringExtra("notification_topic"))
                .setContentText(intent?.getStringExtra("notification_text"))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)

        intent?.removeExtra("notification_text")
        intent?.removeExtra("notification_id")
        intent?.removeExtra("notification_topic")

        val channel = NotificationChannel(ID, channelName, NotificationManager.IMPORTANCE_HIGH)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val intentNew = Intent(context, MainActivity::class.java)
            val pi = PendingIntent.getActivity(context, 0, intentNew, PendingIntent.FLAG_UPDATE_CURRENT)
            channelNotification.setContentIntent(pi)
            manager.createNotificationChannel(channel)
            manager.notify(notificationId,channelNotification.build())
        }


    }
}