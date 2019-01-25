package com.nsb.visions.varun.mynsb.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import eu.amirs.JSON;
import okhttp3.Request;
import okhttp3.Response;

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
    private static int dayAsInt() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());

        // The MyNSB API starts monday at 1 and since java's equiv of 1 is sunday we must then subtract 1 from everything
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static String intToDaystr(int day) {

        // Switch statement for conversion
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
        }

        return "";
    }

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
        }

        return 0;
    }










    /* getDateRange returns the date for the current day along with the date for this week's saturday
         @params;
             String format
                specifies the format the response dates should be in
    */
    @SuppressLint("all")
    public static String[] getDateRange(String format) {
        // Setup a sdf for formatting of the date
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



    /* getDateRangeStart returns the date range for a specific date, it adds a week to the current date and returns the range
            @params;
               String format
                    Specifies the format of the date
     */
    @SuppressLint("all")
    public static String[] getDateRangeStart(String format) {
        // Setup a sdf for formatting the date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        // Setup our calendars
        Calendar future = Calendar.getInstance(TimeZone.getDefault());
        Calendar today = Calendar.getInstance(TimeZone.getDefault());

        // Add seven days to today to get when the week ends
        future.add(DATE,7);

        // Parse our dates
        String futureTxt = simpleDateFormat.format(future);
        String todayTxt = simpleDateFormat.format(today);

        // Return our dates
        return new String[]{todayTxt, futureTxt};
    }


    /* calculateDate returns the day of the week suitable for the api e.g.. 10, 4, 5,
        it sends to the API's week/get feature which returns the week through a calculation made from data on the school's calendar
            @params;
                nil
     */
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




    /* weekAorB tells us if it is week a or b using the API
            @params;
                Context context
     */
    public static String weekAorB(Context context) {
        // Setup a http client
        HTTP httpclient = new HTTP(context);
        final String[] responseStr = {null};


        Thread requester = new Thread(() -> {
            // Setup a request
            Request request = new Request.Builder()
                .get()
                .url(HTTP.API_URL + "/week/get")
                .header("Connection", "close")
                .build();


            try {
                Response response = httpclient.performRequest(request);
                // Parse the response into JSON
                JSON json = new JSON(response.body().string());
                responseStr[0] = json.key("Message").key("Body").stringValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        requester.start();
        try {
            requester.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return responseStr[0];
    }




    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
            = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}