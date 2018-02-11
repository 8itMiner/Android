package com.nsb.visions.varun.mynsb.Reminders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;

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
    public static HTTP httpHandler;
    public static Response response;





    /* createReminder takes a reminder and some context and creates a reminder from both the context and the reminder
        @params;
            Context context
            Reminder reminder

     */
    public static Response createReminder(Context context, Reminder reminder) throws Exception {
        // This is very unnecessary but i feel as if i might beed to access
        // the http handler in the future
        httpHandler = new HTTP(context);

        // Convert our tags into a json string
        String tagsJson = JSON.create(reminder.tags).stringValue();

        // Covert our date into a format accepted by the API
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String date = simpleDateFormat.format(reminder.time);

        // Set up the request body to be tagged along with our request
        RequestBody requestBody = new FormBody.Builder()
            .add("Body", reminder.body)
            .add("Subject", reminder.subject)
            .add("Reminder_Date_Time", date)
            .add("Tags", tagsJson)
            .build();

        // Set up the request to be sent
        Request request = new Request.Builder()
            .url("http://35.189.45.152:8080/api/v1/reminders/Create")
            .post(requestBody)
            .build();

        final Exception[] exception = {null};

        // Send the request and get the response
        Thread sendRequest = new Thread(() -> {
            try {
                response = httpHandler.performRequest(request);
            } catch (Exception e) {
                e.printStackTrace();
                exception[0] = e;
            }
        });
        sendRequest.start();

        if (exception[0] != null) {
            throw exception[0];
        }


        return response;
    }

}
