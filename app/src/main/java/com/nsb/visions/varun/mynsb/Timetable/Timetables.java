package com.nsb.visions.varun.mynsb.Timetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eu.amirs.JSON;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 */

public class Timetables extends Loader<Subject> {

    private SharedPreferences preferences;
    private String url = "";
    private boolean expandOrNot;
    private String adapterTitle;
    private String dayStr;
    private JSON belltimes;




    // NOTE: expandOrNot tells us if this is being used in the expandedTimetables view or the regular timetable view
    public Timetables(Context context, SharedPreferences preferences, Boolean expandOrNot) {
        super(context, Timetables.class);

        // Determine what day of the week it is as a string
        int day = Util.calculateDay(context) + 1;
        int dayOfWeek = day > 5 ? day - 5 : day;
        String week = Util.weekAorB(this.context);

        if (day > 5) {
            week = "B";
        }
        // This is our title for the adapter
        String combined = Util.intToDaystr(dayOfWeek) + " " + week;
        this.adapterTitle = combined;
        // Determine the day as an str
        this.dayStr = Util.intToDaystr(day);

        this.preferences = preferences;
        this.expandOrNot = expandOrNot;
        this.belltimes = new JSON(getBelltimes(context, preferences)).key("Body").index(0).key(dayStr);
    }






    @Override
    @SuppressLint("all")
    public Response sendRequest() {
        try {
            // CreateReminder a http client from the parsed context
            HTTP httpClient = new HTTP(this.context);

            //Response syncedTimetables = getSyncedTimetables(httpClient);
            //// Return synced timetables if it is not null
            //if (syncedTimetables != null) {
            //    return syncedTimetables;
            //}

            // Get the day
            int day = Util.getDay(this.preferences, this.context);
            Log.d("exception-request", String.valueOf(day));
            // Build the url
            this.url = "http://35.189.45.152:8080/api/v1/timetable/Get?Day=" + String.valueOf(day);
            Log.d("expception-request", "http://35.189.45.152:8080/api/v1/timetable/Get?Day=" + String.valueOf(day));

            // Set up the request
            Request request = new Request.Builder()
                .get()
                .url(this.url)
                .build();

            Log.d("exception-timetables", httpClient.performRequest(request).body().string());

            return httpClient.performRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    private Response getSyncedTimetables(HTTP httpHandler) throws Exception {
        // Make a fake request coz that seems to be the only way we can get a response object
        Request request = new Request.Builder().build();
        Response response = httpHandler.performRequest(request);
        // Simple date format for parsing dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        // First lets check the shared preferences to determine if there already is a synced timetable
        String lastUpdateStr = preferences.getString("timetables{tables{last-update}}", "");
        String timetable = preferences.getString("timetables{tables}", "{\"Body\": []}");
        Date lastUpdate = dateFormat.parse(lastUpdateStr);
        Date today = Calendar.getInstance().getTime();
        // Make sure we only have the day month year format
        today = dateFormat.parse(dateFormat.format(today));

        // Determine if can return the synced result or not
        if (!(today.after(lastUpdate))) {
            MediaType contentType = MediaType.parse("text/json");
            ResponseBody body = ResponseBody.create(contentType, timetable);

            return response.newBuilder().body(body).build();
        }

        return null;
    }







    // Parse the json data
    @Override
    public Subject parseJson(JSON jsonRaw, int position) throws Exception {
        // Get the right subject
        JSON json = jsonRaw.index(position);

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
        return new TimetableAdapter(subjects, this.adapterTitle, this.dayStr, preferences, context);
    }








    // Little helper function that will allow us to set a different url to retrieve our timetables from
    public void setURL(String url) {
        this.url = url;
    }








    // get belltimes data
    public static String getBelltimes(Context context, SharedPreferences preferences) {
        // Determine if there is anythin in the shared prefs that we can use
        String belltimes = preferences.getString("belltimes{data}", "");
        // Determine if there really is any data in the belltimes shared prefs if not then pull the data from the api
        if (!belltimes.isEmpty()) {
            return belltimes;
        }

        // Otherwise perform the request from scratch
        final String[] times = {""};
        // Start up a thread
        Thread requestThread = new Thread(() -> {
            HTTP http = new HTTP(context);

            // Setup a request
            Request request = new Request.Builder()
                .get()
                .url("http://35.189.45.152:8080/api/v1/belltimes/Get")
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


        return times[0];
    }
}
