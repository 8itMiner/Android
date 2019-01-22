package com.nsb.visions.varun.mynsb.Jobs;

import android.support.annotation.NonNull;

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

        // Build our timetable instance
        Timetables generator = new Timetables(getContext(), false);
        int day = Util.calculateDay(getContext());
        generator.setDayAndUpdateBellTimes(Util.intToDaystr(day));
        // Get the entire timetables
        List<Subject> timetables = generator.getModels();

        // Iterate over the subjects and create a notification from them
        for (Subject subject : timetables) {
            // Format the string and build the notification
            String data = String.format("Your next period is: %s. With: %s, At: %s", subject.className, subject.teacher, subject.room);
            Notification notification = new Notification(R.drawable.timetable_logo, "MyNSB - Timetable", data);


            // Get the requested time
            String range = generator.belltimes.key(subject.period).stringValue();
            String times[] = range.split(" - ");
            // Parse the first date
            SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

            // Try building & scheduling the notification
            try {
                Date start = dateFormat.parse(times[0]);
                NotifJob.schedule(start, notification);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return DailyJobResult.SUCCESS;
    }



    static public void schedule() {
        DailyJob.schedule(
            new JobRequest.Builder(TAG)
            .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
            .setRequirementsEnforced(true), TimeUnit.HOURS.toMillis(0), TimeUnit.HOURS.toMillis(6));
    }
}
