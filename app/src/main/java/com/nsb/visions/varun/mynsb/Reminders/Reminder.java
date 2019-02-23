package com.nsb.visions.varun.mynsb.Reminders;


import com.nsb.visions.varun.mynsb.Common.Util;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by varun on 17/01/2018.
 */

public class Reminder {
    public String subject;
    public String body;
    public List<String> tags = new ArrayList<>();
    public String date;
    public Date time;

    public Reminder(String subject, String body, JSONArray tags, String time) {
        this.subject = subject;
        this.body = body;
        // Set up the time and add tags
        // Convert the tags into a string array
        for (int i = 0; i < tags.length(); i++) {
            try {
                this.tags.add(tags.getString(i));
            } catch (JSONException e) {
                this.tags.add("Empty");
            }
        }

        this.time = Util.parseDate(time, "yyyy-MM-dd'T'HH:mm:ss'Z'");
        this.date = Util.formateDate(this.time, "yyyy-MM-dd");
    }
}
