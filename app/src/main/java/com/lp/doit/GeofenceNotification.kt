package com.lp.doit

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.GeofencingEvent

class GeofenceNotification: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent.hasError()) {
            if (context != null) {
                Toast.makeText(context, "Geofence alarm error", Toast.LENGTH_SHORT).show()
            }
        } else {
            geofencingEvent.triggeringGeofences.forEach {
                val geofence = it.requestId
                if (context != null) {
                    val onClickIntent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, onClickIntent, 0)

                    val builder = NotificationCompat.Builder(context, "doitchannel")
                        .setSmallIcon(R.drawable.ic_circle)
                        .setContentTitle(intent?.getStringExtra("city"))
                        .setStyle(NotificationCompat.BigTextStyle().bigText("🏃🏃"))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)

                    with(NotificationManagerCompat.from(context)) {
                        // notificationId is a unique int for each notification that you must define
                        notify(2, builder.build())
                    }
                }
            }
        }
    }
}