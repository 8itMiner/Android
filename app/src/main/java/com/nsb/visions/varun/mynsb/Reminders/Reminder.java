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
    public String subject;
    public String body;
    public List<String> tags = new ArrayList<>();
    public String date;
    public Date time;

    @SuppressLint("all")
    public Reminder(String subject, String body, JSONArray tags, String time) {
        this.subject = subject;
        this.body = body;
        // Simpledateformat for parsing reminder dates
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        // Set up the time and add tags
        try {
            // Convert the tags into a string array
            for (int i = 0; i < tags.length(); i++) {
                this.tags.add(tags.getString(i));
            }

            this.time = simpleDateFormat.parse(time);
            // Set the current date
            this.date = dateFormat.format(this.time);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
