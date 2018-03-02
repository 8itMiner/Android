package com.nsb.visions.varun.mynsb.Calendar;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */

// The calendar class represents a simple view of a calendar entry, it contains the basic data required for the calendar MVC RULES!!!!
public class Calendar {

    public String name;
    public String time;

    /* constructor is just a constructor lmao, name = calendar entry, time = calendar entry time
            @params;
                String name
                String tim
     */
    public Calendar(String name, String time) {

        // Convert the date into the format we can
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat postFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Format our date
        Date tempDate = null;
        try {
            tempDate = postFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Parse our new date format
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat returnFormat = new SimpleDateFormat("dd-MM-yyyy");

        // Set the vars
        this.name = name;
        this.time = returnFormat.format(tempDate);

    }

}
