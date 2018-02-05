package com.nsb.visions.varun.mynsb.Events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.nsb.visions.varun.mynsb.Common.Loader;

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
        super(context);
    }

    @Override
    public Response sendRequest() throws Exception {
        // Get the date range required for the api
        String[] dateRange = getDateRange();

        // Set up the http client
        HTTP httpClient = new HTTP(this.context);

        // Setup the url
        assert dateRange != null;
        String requestURL = String.format(Locale.ENGLISH, "http://35.189.45.152:8080/api/v1/Events/Get?start=%s&end=%s",
            dateRange[0], dateRange[1]);

        // Set up the request
        Request request = new Request.Builder()
            .get()
            .url(requestURL)
            .build();

        return httpClient.performRequest(request);
    }


    @Override
    public Event parseJson(JSON json) throws Exception {
        return new Event(json.key("EventID").intValue(), json.key("EventName").stringValue(), json.key("EventStart").stringValue(),
                            json.key("EventEnd").stringValue(), json.key("EventLocation").stringValue(),
                            json.key("EventOrganiser").stringValue(), json.key("EventShortDesc").stringValue(), json.key("EventPictureURL").stringValue());
    }


    @Override
    public RecyclerView.Adapter getAdapterInstance(List<Event> events) {
        return new EventAdapter(events);
    }




    /*
        UTIL FUNCTIONS =================================
     */
    /* getEventRange returns the date for the current day along with the date for this week's saturday
            @params;
                nil
     */
    private String[] getDateRange() {
        // Setup a sdf for formatting of the date
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");

        // Setup our calendars
        Calendar sunday = getInstance(TimeZone.getTimeZone("GMT+10:00"));
        Calendar saturday = getInstance(TimeZone.getTimeZone("GMT+10:00"));

        // Get the respective minmaxes
        sunday.add(DATE, -1 * (DAY_OF_WEEK - SUNDAY));
        saturday.add(DATE, (SATURDAY - DAY_OF_WEEK));

        // Convert these into dates and return it
        String[] dates = new String[2];
        dates[0] = simpleDateFormat.format(sunday.getTime());
        dates[1] = simpleDateFormat.format(saturday.getTime());

        return dates;
    }
    /*
        END UTIL FUNCTIONS =================================
     */
}
