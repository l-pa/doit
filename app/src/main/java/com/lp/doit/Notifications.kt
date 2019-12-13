package com.lp.doit

import android.app.Notification
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

class Notifications : BroadcastReceiver() {

    fun createNotificationChannel(notificationManager: NotificationManager, text: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = text
            val descriptionText = "Todos notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("doitchannel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val onClickIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, onClickIntent, 0)

            val builder = NotificationCompat.Builder(context, "doitchannel")
                .setSmallIcon(R.drawable.ic_circle)
                .setContentTitle(intent?.getStringExtra("name"))
                .setStyle(NotificationCompat.BigTextStyle().bigText(intent?.getStringExtra("description")))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(1, builder.build())
            }
        }
    }
}
