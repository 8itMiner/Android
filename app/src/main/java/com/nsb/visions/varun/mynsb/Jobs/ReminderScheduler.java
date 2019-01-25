package com.nsb.visions.varun.mynsb.Jobs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.nsb.visions.varun.mynsb.Notifications.Notification;
import com.nsb.visions.varun.mynsb.R;
import com.nsb.visions.varun.mynsb.Reminders.Reminder;
import com.nsb.visions.varun.mynsb.Reminders.Reminders;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by varun on 23/01/2019. Coz varun is awesome as hell :)
 */

public class ReminderScheduler extends DailyJob {

    public static final String TAG = "mynsb-reminderScheduler";


    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {

        // Get all the reminders
        Reminders reminders = new Reminders(getContext(), getContext().getSharedPreferences("MyNSB", Context.MODE_PRIVATE));
        List<Reminder> todaysReminders = reminders.getModels();

        // Iterate over and schedule 1 by 1
        for (Reminder reminder : todaysReminders) {
            // Construct a notification object
            Notification notif = new Notification(R.drawable.mynsb_notification_logo, reminder.subject, reminder.body);
            // Send it off the the notif daemon
            NotifJob.schedule(reminder.time, notif, Notification.REMINDER_NOTIF_CHANNEL, Notification.REMINDER_NOTIF_CHANNEL_NAME);
        }

        return DailyJobResult.SUCCESS;
    }



    static public void schedule() {
        DailyJob.schedule(
            new JobRequest.Builder(TAG)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequirementsEnforced(true),
            TimeUnit.HOURS.toMillis(0),
            TimeUnit.HOURS.toMillis(6));
    }
}
