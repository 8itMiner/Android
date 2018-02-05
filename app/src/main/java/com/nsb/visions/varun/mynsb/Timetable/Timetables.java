package com.nsb.visions.varun.mynsb.Timetable;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.List;

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

    @Override
    public Response sendRequest() throws Exception {
        final Response[] response = {null};
        // Start up a request thread
        Thread getTimetable = new Thread(() -> {
            // Create a http client from the parsed context
            HTTP httpClient = new HTTP(context);
            // Set up the request
            Request request = new Request.Builder()
                .get()
                .url("http://35.189.45.152:8080/api/v1/timetable/Get")
                .build();


            // Send the request
            try {
                response[0] = httpClient.performRequest(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // Start it
        getTimetable.start();

        return response[0];
    }

    // Parse the json data
    @Override
    public Subject parseJson(JSON json) throws Exception {
        return new Subject(json.key("class").stringValue(), json.key("room").stringValue(),
            json.key("teacher").stringValue(), json.key("period").intValue());
    }

    @Override
    public RecyclerView.Adapter getAdapterInstance(List<Subject> subjects) {
        return null;
    }





    /*
        UTIL FUNCTIONS ============================
     */
    /* calculateDate returns the day of the week suitable for the api e.g.. 10, 4, 5,
        it sends a request to the school's calendar and determines the day of the week from that
            @params;
                nil

     */
    int calculateDay() {
        return 0;
    }
    /*
        END UTIL FUNCTIONS ============================
     */
}
