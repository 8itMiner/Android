package com.nsb.visions.varun.mynsb.Calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.List;

import eu.amirs.JSON;
import okhttp3.Response;

/**
 */

// The calendars class is the loader for all the current calendars in the API
public class Calendars extends Loader<Calendar> {

    // Constructor
    public Calendars(Context context) {super(context);}




    // sendRequest is the overridden function the Loader class uses to get the API data for a specific view
    @Override
    public Response sendRequest() {
        HTTP httpClient = new HTTP(this.context);

        String[] weekRange = Util.getStartAndEndWeek();
        String[] params = {"Start="+weekRange[0], "End="+weekRange[1]};

        try {
            return httpClient.performRequest(
                httpClient.buildRequest(HTTP.GET, "/events/calendar/get", params), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    // parseJSON parses the specific JSON response from the API
    @Override
    public Calendar parseJson(JSON jsonD, int position) throws Exception {
        JSON json = jsonD.index(position);
        return new Calendar(json.key("title").stringValue(), json.key("start_date").stringValue());
    }




    // getAdapterInstance returns a instance of a recyclerViewAdapter
    @Override
    public RecyclerView.Adapter getAdapterInstance(List<Calendar> calendars) {
        return new CalendarAdapter(calendars);
    }
}
