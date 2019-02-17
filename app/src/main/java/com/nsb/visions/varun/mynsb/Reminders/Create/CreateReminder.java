package com.nsb.visions.varun.mynsb.Reminders.Create;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;
import com.nsb.visions.varun.mynsb.Jobs.NotifJob;
import com.nsb.visions.varun.mynsb.Notifications.Notification;
import com.nsb.visions.varun.mynsb.R;
import com.nsb.visions.varun.mynsb.Reminders.Reminder;

import java.text.SimpleDateFormat;

import eu.amirs.JSON;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by varun on 31/01/2018. Coz varun is awesome as hell :)
 */

// The create class allows users to create a reminder given a bunch of tags, reminder body and the reminder name
public class CreateReminder {

    // Http handler used to send http requests
    @SuppressLint("StaticFieldLeak")
    private static HTTP httpHandler;




    /* createReminder takes a reminder and some context and creates a reminder from both the context and the reminder
        @params;
            Context context
            Reminder reminder

     */
    public static void createReminder(Context context, Reminder reminder) throws Exception {

        // Start the UI handler thread
        final Handler handler = new Handler();


        // This is very unnecessary but i feel as if i might beed to access
        // the http handler in the future
        httpHandler = new HTTP(context);

        // Convert our tags into a json string
        String tagsJson = JSON.create(reminder.tags).stringValue();

        // Covert our date into a format accepted by the API
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String date = simpleDateFormat.format(reminder.time);


        // Set up the request body to be tagged along with our request
        RequestBody requestBody = new FormBody.Builder()
            .addEncoded("Body", reminder.body)
            .addEncoded("Subject", reminder.subject)
            .addEncoded("Date_Time", date)
            .addEncoded("Tags", tagsJson)
            .build();

        // Set up the request to be sent
        Request request = new Request.Builder()
            .url(HTTP.API_URL + "/reminders/create")
            .post(requestBody)
            .build();


        // Send the request and get the response
        Thread sendRequest = new Thread(() -> {
            try {
                Response response = httpHandler.performRequest(request);
                if (!response.isSuccessful()) {
                    handler.post(() -> {
                        Toast.makeText(context, "Could not create reminder. Your date/time was probably in the past!", Toast.LENGTH_LONG).show();
                    });
                    throw new Exception(response.body().string());
                }
                handler.post(() -> {
                    Toast.makeText(context, "Reminder created! Please reload the page.", Toast.LENGTH_LONG).show();
                });


                // Schedule a notification for the reminder
                if (DateUtils.isToday(reminder.time.getTime())) {
                    // Create a notification from our new reminder now
                    Thread createNotif = new Thread(() -> {
                        // Construct the notification
                        Notification notif = new Notification(R.drawable.mynsb_notification_logo, reminder.subject, reminder.body);
                        NotifJob.schedule(reminder.time, notif, Notification.REMINDER_NOTIF_CHANNEL, Notification.REMINDER_NOTIF_CHANNEL_NAME);
                    });
                    createNotif.start();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        sendRequest.start();
    }
}
