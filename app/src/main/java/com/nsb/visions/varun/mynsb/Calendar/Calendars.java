package com.nsb.visions.varun.mynsb.Calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.List;
import java.util.Locale;

import eu.amirs.JSON;
import okhttp3.Request;
import okhttp3.Response;

/**
 */

// The calendars class is the loader for all the current calendars in the API
public class Calendars extends Loader<Calendar> {

    /* constructor is just a constructor lmao
            @params;
                Context context

     */
    public Calendars(Context context) {
        super(context, Calendars.class);
    }



    /*
        OVERRIDDEN METHODS ============================
     */
    /* sendRequest is a function in the loader class, it uses the API to get the desired response
            @params;
                nil
     */
    @Override
    public Response sendRequest() {
        // Get the dates we should use for the request, the start and the end of the week
        String[] dates = Util.getDateRange("dd-MM-yyyy");

        // set up a http client
        HTTP httpClient = new HTTP(this.context);
        // Set up the request
        Request request = new Request.Builder()
            .get()
            .url(String.format(Locale.ENGLISH, "http://35.189.45.152:8080/api/v1/events/Calendar/Get?Start=%s&End=%s", dates[0], dates[1]))
            .build();

        Log.d("content-response", String.format(Locale.ENGLISH, "http://35.189.45.152:8080/api/v1/events/Calendar/Get?Start=%s&End=%s", dates[0], dates[1]));

        // Perform request and return the response
        try {
            return httpClient.performRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /* parseJson is a function in the loader class, it parses the JSON for an individual element
            @params;
                nil
     */
    @Override
    public Calendar parseJson(JSON json) throws Exception {
        return new Calendar(json.key("title").stringValue(), json.key("start_date").stringValue());
    }



    /* getAdapterInstance returns an instance of our associated adapter
            @params;
                List<Model> models
     */
    @Override
    public RecyclerView.Adapter getAdapterInstance(List<Calendar> calendars) {
        return new CalendarAdapter(calendars);
    }

    /*
       END OVERRIDDEN METHODS ============================
     */
}
