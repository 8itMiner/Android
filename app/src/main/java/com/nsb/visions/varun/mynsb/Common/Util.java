package com.nsb.visions.varun.mynsb.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;
import com.nsb.visions.varun.mynsb.Jobs.Dispatchers.JobDispatcher;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

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
    public static int dayAsInt() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                return 1;
            case Calendar.MONDAY:
                return 2;
            case Calendar.TUESDAY:
                return 3;
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

        return 6;
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
    public static int calculateDay(Context context) {
        // Get today as an integer
        int today = dayAsInt() - 1;
        String week = weekAorB(context);
        Log.d("Week-context", week);

        // If today is a saturday or a sunday set it to a monday because there are no timetables for sunday and monday
        if (today == 6 || today == 0) {
            Log.d("week-context", String.valueOf(today));
            today = 1;
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
                .url("http://35.189.45.152:8080/api/v1/week/Get")
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









    /* getDay returns the day cached in our data store
            @params;
                SharedPreferences dataStore
                Context context
            @thorws;
                Exception
     */
    @SuppressWarnings("all")
    public static Integer getDay(SharedPreferences dataStore, Context context) {
        // Read from our shared preferences

        // Attain the day data from our data store, but first determine if this data is outdated or not
        String creation = dataStore.getString("timetables{day{last-update}}", "2018-02-19");

        try {
            // Parse this creation data
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");

            // Parse the date
            Date lastInsert = parser.parse(creation);
            // Get the current date from a calendar singleton
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getDefault());

            // Get the current day
            Date currentDate = calendar.getTime();
            // Format and parse our dates
            Date formattedCurrentDate = parser.parse(parser.format(currentDate));

            // Compare the two dates
            if (formattedCurrentDate.after(lastInsert)) {
                // Call the function to calculate the day for us
                Integer day = Util.calculateDay(context);
                // Update the day
                dataStore.edit().putString("timetables{day{last-update}}", parser.format(formattedCurrentDate));
                dataStore.edit().apply();
                return day;
            }
        } catch (Exception e) {
            return Util.calculateDay(context);

        }

        // Extract the day
        return dataStore.getInt("timetables{day{current-day}}", 1);
    }



}
