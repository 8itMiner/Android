package com.nsb.visions.varun.mynsb.Events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.List;
import java.util.Locale;

import eu.amirs.JSON;
import okhttp3.Request;
import okhttp3.Response;

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
        String requestURL = String.format(Locale.ENGLISH, "/events/get?start=%s&end=%s",
            dateRange[0], dateRange[1]);

        // Set up the request
        Request request = new Request.Builder()
            .get()
            .url(HTTP.API_URL + requestURL)
            .header("Connection", "close")
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

        return new Event(json.key("ID").intValue(), json.key("Name").stringValue(), json.key("Start").stringValue(),
            json.key("End").stringValue(), json.key("Location").stringValue(),
            json.key("Organiser").stringValue(), json.key("ShortDesc").stringValue(), json.key("LongDesc").toString(), json.key("PictureURL").stringValue());

    }


    @Override
    public RecyclerView.Adapter getAdapterInstance(List<Event> events) {
        return new EventAdapter(events, this.context);
    }
}
