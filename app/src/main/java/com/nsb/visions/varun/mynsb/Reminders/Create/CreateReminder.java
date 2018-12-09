package com.nsb.visions.varun.mynsb.Reminders.Create;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;
import com.nsb.visions.varun.mynsb.Reminders.Reminder;

import java.text.SimpleDateFormat;

import eu.amirs.JSON;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
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

        Log.d("Form-Data", reminder.body + ", " + reminder.subject + ", " + date + ", " + tagsJson);

        // Set up the request body to be tagged along with our request
        RequestBody requestBody = new FormBody.Builder()
            .addEncoded("Body", reminder.body)
            .addEncoded("Subject", reminder.subject)
            .addEncoded("Reminder_Date_Time", date)
            .addEncoded("Tags", tagsJson)
            .build();

        // Set up the request to be sent
        Request request = new Request.Builder()
            .url("http://35.189.50.185:8080/api/v1/reminders/Create")
            .post(requestBody)
            .build();


        // Send the request and get the response
        Thread sendRequest = new Thread(() -> {
            try {
                response = httpHandler.performRequest(request);
                Log.d("data-resp", response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        sendRequest.start();

        assert response != null;
        if (!response.isSuccessful()) {
            throw new Exception("Request didn't work");
        }


        return response;
    }

}
