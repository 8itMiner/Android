package com.nsb.visions.varun.mynsb.Timetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.Collections;
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

    private SharedPreferences preferences;
    private String url = null;
    private boolean expandOrNot;

    public Timetables(Context context, SharedPreferences preferences, Boolean expandOrNot) {
        super(context, Timetables.class);
        this.preferences = preferences;
        this.expandOrNot = expandOrNot;
    }

    @Override
    public Response sendRequest() {
        // CreateReminder a http client from the parsed context
        HTTP httpClient = new HTTP(context);

        // Get the day
        int day = Util.getDay(this.preferences, context);

        // TODO: Kind of a hack, needs to be removed later
        if (this.url == null) {
            this.url = "http://35.189.45.152:8080/api/v1/timetable/Get?Day=" + String.valueOf(day);
        }

        // Set up the request
        Request request = new Request.Builder()
            .get()
            .url(url)
            .build();

        try {
            return httpClient.performRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        int day = Util.calculateDay(context) + 1;
        int dayOfWeek = day > 5 ? day - 5 : day;
        String week = Util.weekAorB(this.context);

        if (day > 5) {
            week = "B";
        }

        String combined = Util.intToDaystr(dayOfWeek) + " " + week;

        if (expandOrNot) {
            combined = null;
        }

        return new TimetableAdapter(subjects, combined, Util.intToDaystr(dayOfWeek), preferences, context);
    }


    // Override the loop function
    @Override
    public void loopResults(List<Subject> dest, JSON dataArray) {
        int prev = 0;

        for (int i = 0; i < dataArray.count(); i++) {
            int period = dataArray.index(i).key("Period").intValue();

            // First add in the frees
            int numberOfFrees = period - prev - 1;

            // Add the free periods
            for (int j = 0; j < numberOfFrees; j++) {
                dest.add(prev + j, new Subject("10FREE", "", "", String.valueOf(prev + j)));
            }

            // Add the period
            dest.add(period, new Subject(dataArray.index(i).key("Subject").stringValue(), dataArray.index(i).key("ClassRoom").stringValue(),
                dataArray.index(i).key("Teacher").stringValue(), String.valueOf(period)));

            // Determine its period position in the timetable
            prev = period;
        }
        // Trim our list
        dest.removeAll(Collections.singleton(null) );
    }


    // Little helper function that will allow us to set a different url to retrieve our timetables from
    public void setURL(String url) {
        this.url = url;
    }
}
