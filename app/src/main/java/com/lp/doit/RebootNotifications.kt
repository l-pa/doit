package com.lp.doit

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import java.util.*


class RebootNotifications : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
                // on device boot complete, reset the alarm

            }
        }
    }
}