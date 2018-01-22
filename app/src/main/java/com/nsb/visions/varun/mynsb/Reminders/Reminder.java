package com.nsb.visions.varun.mynsb.Reminders;


import android.annotation.SuppressLint;

import org.json.JSONArray;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by varun on 17/01/2018. Coz varun is awesome as hell :)
 */

public class Reminder {
    String subject;
    String body;
    List<String> tags;
    Date time;

    public Reminder(String subject, String body, JSONArray tags, String time) throws Exception {
        this.subject = subject;
        this.body = body;
        // Convert the time string into a calendar
        // Split up the time
        String[] dateTime = time.split("T");
        String rawTime = dateTime[1].split(":00Z")[0];
        String date = dateTime[0];

        // Setup a simple date format
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        // Set up the time

        // Convert the tags into a string array
        for (int i = 0; i < tags.length(); i++) {
            this.tags.add(tags.getString(i));
        }

        this.time = simpleDateFormat.parse(date + " " + rawTime);

    }
}
