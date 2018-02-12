package com.nsb.visions.varun.mynsb.Calendar;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by varun on 9/02/2018. Coz varun is awesome as hell :)
 */

public class Calendar {

    public String name;
    public String time;

    // Constructor for a calendar element
    public Calendar(String name, String time) {

        // Conver the date into the format we can
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
