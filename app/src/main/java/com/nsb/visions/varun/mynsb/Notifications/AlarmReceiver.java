package com.nsb.visions.varun.mynsb.Notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by varun on 13/02/2018. Coz varun is awesome as hell :)
 */

public class AlarmReceiver extends BroadcastReceiver {


    public static final String CUSTOM_INTENT = "com.intent.action.ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {
        /* Queue the job in our jobIntentService */
        NotificationsJobIntentService.enqueueWork(context, intent);
    }

    // Cancel an alarm
    public static void cancelAlarm(Context context) {
        // Set up an intent
        Intent intent = new Intent(context, NotificationsJobIntentService.class);
        // Get the alarm
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Attain the sender
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        /* cancel any pending alarm */
        alarm.cancel(sender);
    }

}


