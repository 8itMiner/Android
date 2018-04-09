package com.nsb.visions.varun.mynsb.Jobs;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobRequest;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.Jobs.NotificationDispatchers.NotificationDispatcher;
import com.nsb.visions.varun.mynsb.Jobs.NotificationDispatchers.TimetableDispatcher;
import com.nsb.visions.varun.mynsb.Timetable.Subject;
import com.nsb.visions.varun.mynsb.Timetable.Timetables;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;

/**
 */

public class TimetableSync extends DailyJob {

    public static final String TAG = "timetable.sync-job";
    private Context context;
    private SharedPreferences preferences;


    public TimetableSync(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;
    }


    @NonNull
    @Override
    @SuppressLint("all")
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {

        // Download our timetables and put it in our shared preferences
        new Thread(() -> {
            try {
                Timetables timetables = new Timetables(context, preferences, null);

                String data = downloadTimetables(timetables);
                downloadBellTimes();

                // Get the current date
                Date currentTime = Calendar.getInstance().getTime();
                // SDF for parsing
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

                // Start parsing our timetable data into shared preferences
                preferences.edit().putString("timetables{tables}", data);
                preferences.edit().putString("timetables{tables{last-update}}", simpleDateFormat.format(currentTime));
                preferences.edit().apply();
                // Perform the get day function so the days sync too
                Util.getDay(preferences, context);

                // Create an instance so we can create all our notifications
                TimetableDispatcher dispatcher = new TimetableDispatcher(this.context, this.preferences);
                // The abstracted class will get the subjects for us
                dispatcher.scheduleNotifications();



            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return DailyJobResult.SUCCESS;
    }



    private String downloadTimetables(Timetables timetables) throws IOException {
        // Get the timetables
        Response timetableData = timetables.sendRequest();
        return timetableData.body().string();
    }


    @SuppressLint("CommitPrefEdits")
    private void downloadBellTimes() {
        // Function to download belltimes from the API
        String belltimes = Timetables.getBelltimes(this.context, this.preferences);
        // Sync the belltimes in shared preferences
        this.preferences.edit().putString("belltimes{data}", belltimes);
        this.preferences.edit().apply();
    }


    public static void scheduleTask() {
        // Schedule the sync to happen from 1am - 2am
        DailyJob.schedule(new JobRequest.Builder(TimetableSync.TAG), TimeUnit.HOURS.toMillis(1), TimeUnit.HOURS.toMillis(2));
    }

}
