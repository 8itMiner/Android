package com.nsb.visions.varun.mynsb.Jobs;

import android.support.annotation.NonNull;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobRequest;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.Notifications.Notification;
import com.nsb.visions.varun.mynsb.R;
import com.nsb.visions.varun.mynsb.Timetable.Subject;
import com.nsb.visions.varun.mynsb.Timetable.Timetables;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by varun on 22/01/2019. Coz varun is awesome as hell :)
 */

public class TimetableScheduler extends DailyJob {

    public static final String TAG = "mynsb-timetableScheduler";

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {

        // Dont run this job on saturdays or sundays
        Calendar cal = Calendar.getInstance();
        int DOY = cal.get(Calendar.DAY_OF_WEEK);
        if (DOY == Calendar.SUNDAY || DOY == Calendar.SATURDAY) {
            return DailyJobResult.SUCCESS;
        }

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
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

            // Try building & scheduling the notification
            try {

                Date start = dateFormat.parse(times[0]);

                // We need to sechedule it for today
                Calendar calendar = Calendar.getInstance();
                Calendar backup = Calendar.getInstance();

                calendar.setTime(start);
                calendar.set(backup.get(Calendar.YEAR), backup.get(Calendar.MONTH), backup.get(Calendar.DAY_OF_MONTH));

                NotifJob.schedule(calendar.getTime(), notification, Notification.TIMETABLE_NOTIF_CHANNEL, Notification.TIMETABLE_NOTIF_CHANNEL_NAME);
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
            .setRequirementsEnforced(true), TimeUnit.HOURS.toMillis(5), TimeUnit.HOURS.toMillis(8));
    }
}
