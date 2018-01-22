package com.nsb.visions.varun.mynsb.Events;

import android.content.Context;

import com.nsb.visions.varun.mynsb.Common.Loader;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.List;

import eu.amirs.JSON;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by varun on 21/01/2018. Coz varun is awesome as hell :)
 */

public class Events extends Loader<Event> {

    public Events(Context context) {
        super(context);
    }

    @Override
    public Response sendRequest() throws Exception {
        // Response array for retrieving our response
        final Response[] response = new Response[1];

        Thread perfRequest = new Thread(() -> {
            // Set up the http client
            HTTP httpClient = new HTTP(this.context);

            // Set up the request
            Request request = new Request.Builder()
                .get()
                .url("http://35.189.45.152:8080/api/v1/Events/Get")
                .build();

            // Get the response from the http client
            try {
                response[0] = httpClient.performRequest(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return response[0];
    }

    @Override
    public Event parseJson(JSON json) throws Exception {
        return new Event(json.key("EventID").intValue(), json.key("EventName").stringValue(), json.key("EventStart").stringValue(),
                            json.key("EventEnd").stringValue(), json.key("EventLocation").stringValue(),
                            json.key("EventOrganiser").stringValue(), json.key("EventShortDesc").stringValue(), json.key("EventPictureURL").stringValue());
    }


    @Override
    public void getAdapterInstance(List<Event> events) {
        this.adapter = new EventAdapter(events);
    }
}
