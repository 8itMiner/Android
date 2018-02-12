package com.nsb.visions.varun.mynsb.Timetable;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eu.amirs.JSON;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by varun on 31/01/2018. Coz varun is awesome as hell :)
 */

public class Timetables extends Loader<Subject> {

    public Timetables(Context context) {
        super(context);
    }
    public String week;

    @Override
    public Response sendRequest() throws Exception {
        // CreateReminder a http client from the parsed context
        HTTP httpClient = new HTTP(context);

        // Get the day
        int day = calculateDay();

        // Set up the request
        Request request = new Request.Builder()
            .get()
            .url("http://35.189.45.152:8080/api/v1/timetable/Get?Day=" + String.valueOf(day))
            .build();

        return httpClient.performRequest(request);
    }

    // Parse the json data
    @Override
    public Subject parseJson(JSON json) throws Exception {
        return new Subject(json.key("Subject").stringValue(), json.key("ClassRoom").stringValue(),
            json.key("Teacher").stringValue(), json.key("Period").stringValue());
    }

    // Return an instance of our adapter
    @Override
    public RecyclerView.Adapter getAdapterInstance(List<Subject> subjects) {
        // Determine what day of the week it is as a string
        int day = calculateDay();
        int dayOfWeek = day > 5 ? day - 5 : day;
        return new TimetableAdapter(subjects, Util.intToDaystr(dayOfWeek) + " " + this.week);
    }





    /*
        UTIL FUNCTIONS ============================
     */
    /* calculateDate returns the day of the week suitable for the api e.g.. 10, 4, 5,
        it sends to the API's week/get feature which returns the week through a calculation made from data on the school's calendar
            @params;
                nil

     */
    private int calculateDay() {
        // Get today as an integer
        int today = Util.dayAsInt() - 1;
        String week = "";
        try {
            week = weekAorB(this.context);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // If today is a saturday or a sunday set it to a monday because there are no timetables for sunday and monday
        if (today == 6 || today == 0) {
            today = 1;
            week = week.equals("A") ? "B" : "A";
        }

        // Determine if we need to +5 or if we just leave it as is
        if (week.equals("B")) {
            today += 5;
        }


        // We need to set the week value for the getAdapterInstance function
        this.week = week;
        // Return the day
        return today;
    }


    /* weekAorB tells us if it is week a or b using the API
            @params;
                Context context
     */
    private String weekAorB(Context context) throws Exception {
        // Send a request to the API
        HTTP client = new HTTP(context);

        // Set up a request
        Request request = new Request.Builder()
            .get()
            .url("http://35.189.45.152:8080/api/v1/week/Get")
            .build();


        // Attain a response
        Response response = client.performRequest(request);

        // Get the data from the response and begin parsing it
        JSON json = new JSON(response.body().string());


        // Convert the week in respect to the day
        return json.key("Message").key("Body").stringValue();
    }

    /*
        END UTIL FUNCTIONS ============================
     */
}
