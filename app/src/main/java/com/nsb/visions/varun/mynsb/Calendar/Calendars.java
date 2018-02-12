package com.nsb.visions.varun.mynsb.Calendar;

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
 */

public class Calendars extends Loader<Calendar> {

    // Construct
    public Calendars(Context context) {
        super(context);
    }



    /*
        OVERRIDDEN METHODS ============================
     */
    @Override
    public Response sendRequest() throws Exception {
        // Get the dates we should use for the request, the start and the end of the week
        String[] dates = Util.getDateRange("dd-MM-yyyy");

        // set up a http client
        HTTP httpClient = new HTTP(this.context);
        // Set up the request
        Request request = new Request.Builder()
            .get()
            .url(String.format(Locale.ENGLISH, "http://35.189.45.152:8080/api/v1/events/Calendar/Get?Start=%s&End=%s", dates[0], dates[1]))
            .build();

        // Perform request and return the response
        return httpClient.performRequest(request);
    }

    @Override
    public Calendar parseJson(JSON json) throws Exception {
        return new Calendar(json.key("title").stringValue(), json.key("start_date").stringValue());
    }

    @Override
    public RecyclerView.Adapter getAdapterInstance(List<Calendar> calendars) {
        return new CalendarAdapter(calendars);
    }

    /*
       END OVERRIDDEN METHODS ============================
     */
}
