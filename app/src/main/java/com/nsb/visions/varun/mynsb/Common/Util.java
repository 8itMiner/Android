package com.nsb.visions.varun.mynsb.Common;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static java.util.Calendar.DATE;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.getInstance;

/**
 * Created by varun on 8/02/2018. Coz varun is awesome as hell :)
 */

public class Util {

    /* dayAsInt returns the current day as an integer
        @params;
            nil
    */
    public static int dayAsInt() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                return 1;
            case Calendar.MONDAY:
                return 2;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 4;
            case Calendar.THURSDAY:
                return 5;
            case Calendar.FRIDAY:
                return 6;
            case Calendar.SATURDAY:
                return 7;
        }
        return 0;
    }

    public static String intToDaystr(int day) {

        // Switch statement for conversion
        switch (day) {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
        }

        return "";
    }



    /* getEventRange returns the date for the current day along with the date for this week's saturday
         @params;
             String format
                specifies the format the response dates should be in
    */
    public static String[] getDateRange(String format) {
        // Setup a sdf for formatting of the date
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        // Setup our calendars
        // Determine the sunday week
        Calendar sunday = getInstance(TimeZone.getDefault());
        // Get the respective minmaxes
        sunday.add(DATE, (SUNDAY - Util.dayAsInt()));
        String sundayTxt = simpleDateFormat.format(sunday.getTime());

        Calendar saturday = getInstance(TimeZone.getDefault());
        saturday.add(DATE, (SATURDAY - Util.dayAsInt()));
        String saturdayTxt = simpleDateFormat.format(saturday.getTime());

        // Convert these into dates and return it
        return new String[]{sundayTxt, saturdayTxt};
    }

}
