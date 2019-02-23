package com.nsb.visions.varun.mynsb.Reminders.Create;

import android.content.Context;
import android.os.Handler;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;
import com.nsb.visions.varun.mynsb.Jobs.NotifJob;
import com.nsb.visions.varun.mynsb.Notifications.Notification;
import com.nsb.visions.varun.mynsb.R;
import com.nsb.visions.varun.mynsb.Reminders.Reminder;

import java.io.IOException;

import eu.amirs.JSON;
import okhttp3.Response;

/**
 * Created by varun on 31/01/2018.
 */

// The create class allows users to create a reminder given a bunch of tags, reminder body and the reminder name
public class CreateReminder {

    // createReminder takes a reminder and some context and creates a reminder from both the context and the reminder
    public static void create(Context context, Reminder reminder) {

        final Handler handler = new Handler();
        final HTTP httpHandler = new HTTP(context);

        // Convert our tags into a json string
        String tagsJson = JSON.create(reminder.tags).stringValue();
        // Covert our date into a format accepted by the API
        String date = Util.formateDate(reminder.time, "dd-MM-yyyy HH:mm");

        Thread createReminder = new Thread(() -> {
            try {
                // Set up the request body to be tagged along with our request
                String[] postBody = {"Body=" + reminder.body, "Subject=" + reminder.subject,
                                     "Date_Time=" + date, "Tags=" + tagsJson};
                // Set up the request to be sent
                Response response = httpHandler.performRequest(
                    httpHandler.buildRequest(HTTP.POST, "/reminders/create", postBody),
                    false);

                dealWithRequestResp(response.isSuccessful(), handler, context);
                createReminderNotif(reminder);

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
        createReminder.start();
    }




    // dealWithRequestResp deals with the output from the HTTP response
    private static void dealWithRequestResp(boolean responseSuccess, Handler handler, Context context) {
        if (!responseSuccess) {
            handler.post(() -> {
                Toast.makeText(context, "Could not create reminder. Your date/time was probably in the past!", Toast.LENGTH_LONG).show();
            });
        }
        handler.post(() -> {
            Toast.makeText(context, "Reminder created! Please reload the page.", Toast.LENGTH_LONG).show();
        });
    }




    // createReminderNotif generates a notification based off the reminder that has just been made
   private static  void createReminderNotif(Reminder reminder) {
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
   }
}
