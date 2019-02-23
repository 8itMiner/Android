package com.nsb.visions.varun.mynsb.Common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import eu.amirs.JSON;
import okhttp3.Response;

import static java.util.Calendar.DATE;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.getInstance;

/**
 * Created by varun on 8/02/2018
 */

// Simple utility class
public class Util {


    // dayAsInt returns the curret day as an integer, NOTE: Monday integer equivalent is 1
    private static int dayAsInt() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());

        // The MyNSB API starts monday at 1 and since java's equiv of 1 is sunday we must then subtract 1 from everything
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }




    // intToDayStr takes an integer and converts it into a Day string, NOTE: this is meant to be used with Java's calendar class which sets Monday's integer value to 2
    public static String intToDaystr(int day) {
        switch (day) {
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            default:
                return "";
        }
    }




    // stringToDayInt takes a day as a String and returns its integer representation NOTE: This is meant to be used with the myNSB API where Monday is 1
    public static int stringToDayInt(String day) {
        switch (day) {
            case "Monday":
                return 1;
            case "Tuesday":
                return 2;
            case "Wednesday":
                return 3;
            case "Thursday":
                return 4;
            case "Friday":
                return 5;
            default:
                return 0;
        }
    }




    // getStartAndEndWeek returns the date for the current week's Sunday along with the date for this week's Saturday, first index is Sunday, last index is Saturday
    public static String[] getStartAndEndWeek() {

        SimpleDateFormat baseFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        Calendar sunday = getInstance(TimeZone.getDefault());
        sunday.add(DATE, (SUNDAY - Util.dayAsInt()));
        String sundayTxt = baseFormat.format(sunday.getTime());

        Calendar saturday = getInstance(TimeZone.getDefault());
        saturday.add(DATE, (SATURDAY - Util.dayAsInt()));
        String saturdayTxt = baseFormat.format(saturday.getTime());

        return new String[]{sundayTxt, saturdayTxt};
    }




    // calculateDate returns the day of the week suitable for the api e.g.. 10, 4, 5,
    // it sends to the API's week/get feature which returns the week through a calculation made from data on the school's calendar
    public static int calculateDay(String week) {
        int today = dayAsInt();
        // If today is a saturday or a sunday set it to a monday because there are no timetables for sunday and monday
        if (today == 6 || today == 0) {
            today = 1;
            // Reset our week, e.g if its week b on a sunday set it to week a for the monday
            if (Objects.equals(week.trim(), "A")) {
                week = "B";
            } else {
                week = "A";
            }
        }
        // Determine if we need to +5 or if we just leave it as is
        if (week.equals("B")) {
            today += 5;
        }
        // Return the day
        return today;
    }




    // weekAorB tells us if it is week a or b using the API
    public static String weekAorB(Context context) {
        // Setup a http client
        HTTP httpclient = new HTTP(context);

        try {
            Response response = httpclient.performRequest(
                httpclient.buildRequest(HTTP.GET,  "/week/get", null), false);
            // Parse the response into JSON
            JSON json = new JSON(response.body().string());
            return json.key("Message").key("Body").stringValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }




    // showErrors determine if error should be exposed to a user or not
    public static void showErrors(RecyclerView recyclerView, TextView errorHolder, boolean showError) {
        if (showError) {
            recyclerView.swapAdapter(new RecyclerViewEmpty(), true);
            errorHolder.setVisibility(View.VISIBLE);
        } else {
            errorHolder.setVisibility(View.GONE);
        }
    }




    // parseDate parses a specific date into the format used throughout the app
    public static Date parseDate(String date, String format) {
        SimpleDateFormat postFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date tempDate = Calendar.getInstance().getTime();
        try {
            tempDate = postFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tempDate;
    }




    // formateDate takes a date and formats it into everyone's favourite API format
    public static String formateDate(Date date, String format) {
        SimpleDateFormat returnFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return returnFormat.format(date);
    }
}