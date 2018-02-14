package com.nsb.visions.varun.mynsb.Notifications;


import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import eu.amirs.JSON;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by varun on 13/02/2018. Coz varun is awesome as hell :)
 */

public class NotificationsJobIntentService extends JobIntentService {

    // Unique job id for the service
    private static final int JOB_ID = 3421;
    private static Context context;

    public static void enqueueWork(Context ctx, Intent intent) {
        enqueueWork(ctx, NotificationsJobIntentService.class, JOB_ID, intent);
        context = ctx;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // Construct and sdf for parsing the dates that the api gives us
        SimpleDateFormat reminderSdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        new Thread(() -> {
            // Download our data from the API
            // BellTimes
            JSON bellTimes = getBellTimes(context);
            // Get the timetables
            JSON timetables = getTimetables(context);
            // Get the reminders for today
            JSON reminders = getReminders(context);

            // Begin building notifications from the reminders
            for (int i = 0; i < reminders.count(); i++) {
                JSON reminder = reminders.index(i);
                // Attain the reminder datetime
                Date reminderTime = null;
                try {
                    reminderTime = reminderSdf.parse(reminder.key("ReminderDateTime").stringValue());
                    // Build a notification from that
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }




        }).start();
    }


    // Function to get the belltimes from the API
    private JSON getBellTimes(Context context) {
        return sendRequest("35.189.45.152:8080/api/v1/belltimes/Get", context);
    }


    private JSON getTimetables(Context context) {
        // Attain the day
        int day = Util.calculateDay(context);

        // Get the response
        return sendRequest("http://35.189.45.152:8080/api/v1/timetable/Get?Day=" + String.valueOf(day), context);
    }

    // Function to get reminders
    private JSON getReminders(Context context) {
        return sendRequest("35.189.45.152:8080/api/v1/reminders/Get/Today", context);
    }


    private JSON sendRequest(String url, Context context) {
        HTTP httpclient = new HTTP(context);

        // Build a request
        Request request = new Request.Builder()
            .get()
            .url(url)
            .build();

        try {
            Response response = httpclient.performRequest(request);
            JSON json = new JSON(response.body().string());

            return json.key("Message").key("Body").index(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
