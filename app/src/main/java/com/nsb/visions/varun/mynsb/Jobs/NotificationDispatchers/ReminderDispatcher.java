package com.nsb.visions.varun.mynsb.Jobs.NotificationDispatchers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Reminders.Reminder;
import com.nsb.visions.varun.mynsb.Reminders.Reminders;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 */

public class ReminderDispatcher extends NotificationDispatcher<Reminder> {

    // Construct with super
    public ReminderDispatcher(Context context, SharedPreferences preferences) {
        super(context, preferences);
    }


    // Overridden methods
    @Override
    Loader<Reminder> getLoaderInstance() {
        return new Reminders(this.context, this.preferences);
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    String getNotificationTime(Reminder reminder) {
        // Construct an ADF for formatting our notification string
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm pm");
        return simpleDateFormat.format(reminder.time);
    }

    @Override
    HashMap<String, String> getNotificationDetails(Reminder reminder) {
        HashMap<String, String> notificationDetails = new HashMap<>();
        // Start popping our details into the hashmap
        notificationDetails.put("Title", reminder.subject);
        notificationDetails.put("Body", reminder.body);
        return notificationDetails;
    }
}
