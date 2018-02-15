package com.nsb.visions.varun.mynsb.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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



    /* calculateDate returns the day of the week suitable for the api e.g.. 10, 4, 5,
        it sends to the API's week/get feature which returns the week through a calculation made from data on the school's calendar
            @params;
                nil

     */
    public static int calculateDay(Context context) {
        // Get today as an integer
        int today = dayAsInt() - 1;
        String week = weekAorB(context);

        // If today is a saturday or a sunday set it to a monday because there are no timetables for sunday and monday
        if (today == 6 || today == 0) {
            today = 1;
            week = week.equals("A") ? "B" : "A";
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
       GetWeek getter = new GetWeek(context);
        try {
            return getter.execute("http://35.189.45.152:8080/api/v1/week/Get").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }




    // Async class for determining if it is week a or b
    private static class GetWeek extends AsyncTask<String, Integer, String> {

        private Context context;

        public GetWeek(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];

            // Setup a http handler
            HTTP client = new HTTP(context);

            // Set up a request
            Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

            try {
                // Get the response
                Response response = client.performRequest(request);

                // Get the data from the response and begin parsing it
                JSON json = new JSON(response.body().string());
                return json.key("Message").key("Body").stringValue();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
