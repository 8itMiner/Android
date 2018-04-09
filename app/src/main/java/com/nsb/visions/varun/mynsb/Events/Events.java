package com.nsb.visions.varun.mynsb.Events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nsb.visions.varun.mynsb.Common.Loader;

import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import eu.amirs.JSON;
import okhttp3.Request;
import okhttp3.Response;

import static java.util.Calendar.*;

/**
 * Created by varun on 21/01/2018. Coz varun is awesome as hell :)
 */

public class Events extends Loader<Event> {

    public Events(Context context) {
        super(context, Events.class);
    }



    @Override
    public Response sendRequest() {
        // Get the date range required for the api
        String[] dateRange = Util.getDateRange("dd-MM-yyyy");

        // Set up the http client
        HTTP httpClient = new HTTP(this.context);

        // Build a request from the data we have
        String requestURL = String.format(Locale.ENGLISH, "http://35.189.45.152:8080/api/v1/events/Get?start=%s&end=%s",
            dateRange[0], dateRange[1]);

        // Set up the request
        Request request = new Request.Builder()
            .get()
            .url(requestURL)
            .build();


        try {
            return httpClient.performRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Event parseJson(JSON bodyBlock, int position) throws Exception {
        JSON json = bodyBlock.index(position);

        return new Event(json.key("EventID").intValue(), json.key("EventName").stringValue(), json.key("EventStart").stringValue(),
            json.key("EventEnd").stringValue(), json.key("EventLocation").stringValue(),
            json.key("EventOrganiser").stringValue(), json.key("EventShortDesc").stringValue(), json.key("EventPictureURL").stringValue());

    }


    @Override
    public RecyclerView.Adapter getAdapterInstance(List<Event> events) {
        return new EventAdapter(events, this.context);
    }
}
