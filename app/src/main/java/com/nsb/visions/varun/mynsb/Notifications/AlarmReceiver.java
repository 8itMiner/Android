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
        // Get the alarm
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        /* cancel any pending alarm */
        assert alarm != null;
        alarm.cancel(getPendingIntent(context));
    }


    public static void setAlarm(Context context, boolean force, long when) {
        // Cancel any pending alarm
        cancelAlarm(context);

        // Setup an alarm manager
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Execute in the next when hours and fire the broadcast
        assert alarm != null;
        alarm.set(AlarmManager.RTC_WAKEUP, when, getPendingIntent(context));
    }


    private static PendingIntent getPendingIntent(Context context) {
        Context ctx;   /* get the application context */
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setAction(CUSTOM_INTENT);

        return PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}


