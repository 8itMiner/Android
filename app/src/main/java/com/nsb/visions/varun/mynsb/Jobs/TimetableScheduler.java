package com.nsb.visions.varun.mynsb.Jobs;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.Notifications.Notification;
import com.nsb.visions.varun.mynsb.R;
import com.nsb.visions.varun.mynsb.Timetable.Subject;
import com.nsb.visions.varun.mynsb.Timetable.Timetables;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import eu.amirs.JSON;

/**
 * Created by varun on 22/01/2019. Coz varun is awesome as hell :)
 */

public class TimetableScheduler extends DailyJob {

    public static final String TAG = "mynsb-timetableScheduler";

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {

        Log.d("Mynsb-notif: ", "dsadsa");

        // Build our timetable instance
        Timetables generator = new Timetables(getContext(), false);
        String week = Util.weekAorB(getContext());
        int day = Util.calculateDay(week);
        generator.setDayAndUpdateBellTimes(Util.intToDaystr(day));
        // Get the entire timetables
        List<Subject> timetables = generator.getModels();

        // Iterate over the subjects and create a notification from them
        for (Subject subject : timetables) {
            // Format the string and build the notification
            String data = String.format("Your next period is: %s. With: %s, At: %s", subject.className, subject.teacher, subject.room);
            Notification notification = new Notification(R.drawable.mynsb_notification_logo, "MyNSB - Timetable", data);


            // Get the requested time
            String range = generator.belltimes.key(subject.period).stringValue();
            String times[] = range.split(" - ");
            // Parse the first date
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            // Try building & scheduling the notification
            try {
                Date start = dateFormat.parse(times[0]);
                NotifJob.schedule(start, notification, Notification.TIMETABLE_NOTIF_CHANNEL, Notification.TIMETABLE_NOTIF_CHANNEL_NAME);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return DailyJobResult.SUCCESS;
    }



    @SuppressLint("LongLogTag")
    static public void schedule() {
        Log.d(TAG, "Attempting to create a mynsb reminder");
        DailyJob.schedule(
            new JobRequest.Builder(TAG)
            .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
            .setRequirementsEnforced(true),
            TimeUnit.HOURS.toMillis(15),
            TimeUnit.HOURS.toMillis(16));
    }
}
