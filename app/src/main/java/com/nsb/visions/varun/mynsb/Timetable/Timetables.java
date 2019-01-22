package com.nsb.visions.varun.mynsb.Timetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.List;

import eu.amirs.JSON;
import okhttp3.Request;
import okhttp3.Response;

/**
 */

public class Timetables extends Loader<Subject> {

    private String url = "";
    private boolean expandOrNot;
    private String adapterTitle;
    private String dayStr;
    private String week;
    public JSON belltimes;




    // NOTE: expandOrNot tells us if this is being used in the expandedTimetables view or the regular timetable view
    public Timetables(Context context, Boolean expandOrNot) {
        super(context, Timetables.class);

        // Determine what day of the week it is as a string
        int day = Util.calculateDay(context);
        int dayOfWeek = day > 5 ? day - 5 : day;
        String week = Util.weekAorB(this.context);


        // Build the url
        this.url = "http://35.189.50.185:8080/api/v1/timetable/Get?Day=" + String.valueOf(day);
        this.week = week;


        if (day > 5) {
            week = "B";
        }
        // This is our title for the adapter
        this.adapterTitle = Util.intToDaystr(dayOfWeek + 1) + " " + week;
        // Determine the day as an str
        this.dayStr = Util.intToDaystr(day);
        this.expandOrNot = expandOrNot;
        this.belltimes = new JSON(getBelltimes(context)).key("Body").index(0).key(dayStr);
    }






    @Override
    @SuppressLint("all")
    public Response sendRequest() {
        try {
            // CreateReminder a http client from the parsed context
            HTTP httpClient = new HTTP(this.context);


            // Set up the request
            Request request = new Request.Builder()
                .get()
                .url(this.url)
                .header("Connection", "close")
                .build();


            return httpClient.performRequest(request);
        } catch (Exception e) {
            Log.d("Exception", "Boom");
            e.printStackTrace();
        }
        return null;
    }


    // Parse the json data
    @Override
    public Subject parseJson(JSON jsonRaw, int position) throws Exception {
        // Get the right subject
        JSON json = jsonRaw.index(position);
        Log.d("Timetable Data:", json.toString());

        // Determine what belltime to display to the user
        // Start getting the belltimes for each individual person
        int period = json.key("Period").intValue();

        return new Subject(json.key("Subject").stringValue(), json.key("ClassRoom").stringValue(),
            json.key("Teacher").stringValue(), String.valueOf(period), this.belltimes.key(String.valueOf(period)).stringValue());
    }






    // Return an instance of our adapter
    @Override
    public RecyclerView.Adapter getAdapterInstance(List<Subject> subjects) {
        // This tells us if we should expand the timetables or not
        if (expandOrNot) {
            this.adapterTitle = null;
        }
        // Return an adapter instance
        // TODO: ADD BELLTIMES AS PART OF THE SUBJECT CLASS
        return new TimetableAdapter(subjects, this.adapterTitle, context);
    }








    // Little helper function that will allow us to set a different url to retrieve our timetables from
    public void setURL(String url) {
        this.url = url;
    }
    public void setDayAndUpdateBellTimes(String dayStr) {
        this.dayStr = dayStr;
        this.belltimes = new JSON(getBelltimes(context)).key("Body").index(0).key(dayStr);
        Log.d("Belltimes: ", belltimes.toString());
    }
    public void setWeek(String week) {
        this.week = week;
    }
    public String getWeek(){
        return this.week;
    }
    public String getDayStr() {
        return this.dayStr;
    }






    // get belltimes data
    public static String getBelltimes(Context context) {
        // Otherwise perform the request from scratch
        final String[] times = {""};
        // Start up a thread
        Thread requestThread = new Thread(() -> {
            HTTP http = new HTTP(context);

            // Setup a request
            Request request = new Request.Builder()
                .get()
                .url("http://35.189.50.185:8080/api/v1/belltimes/Get")
                .header("Connection", "close")
                .build();

            try {
                times[0] = http.performRequest(request).body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        requestThread.start();
        try {
            requestThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return new JSON(times[0]).key("Message").toString();
    }
}