package com.nsb.visions.varun.mynsb.Reminders;


import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import eu.amirs.JSON;

/**
 * Created by varun on 17/01/2018. Coz varun is awesome as hell :)
 */

public class Reminder {
    String subject;
    String body;
    List<String> tags = new ArrayList<>();
    Date time;

    public Reminder(String subject, String body, JSONArray tags, String time) {
        this.subject = subject;
        this.body = body;
        // Convert the time string into a calendar
        // Split up the time
        String[] dateTime = time.split("T");
        String rawTime = dateTime[1].split(":00Z")[0];
        String date = dateTime[0];

        Log.d("time-data", date + " " + rawTime);

        // Setup a simple date format
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        // Set up the time and add tags
        try {
            // Convert the tags into a string array
            for (int i = 0; i < tags.length(); i++) {
                this.tags.add(tags.getString(i));
            }

            this.time = simpleDateFormat.parse(date + " " + rawTime);
        } catch (Exception e) {
            Log.d("Exception-error", e.toString());
            e.printStackTrace();
        }

    }
}
