package com.nsb.visions.varun.mynsb.Reminders;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.Calendar;
import java.util.List;

import eu.amirs.JSON;
import okhttp3.Response;

/**
 */


//  Reminders loads all reminders for the specific user for *_today_*
public class Reminders extends Loader<Reminder>{

    private SharedPreferences preferences;




    // Constructor
    public Reminders(Context context, SharedPreferences preferences) {
        super(context);
        this.preferences = preferences;
    }



    // sendRequest gets all the reminders
    @Override
    public Response sendRequest() {
        // Set up the http client
        HTTP httpclient = new HTTP(this.context);

        // Get the time extremes
        Calendar calendar = Calendar.getInstance();
        String now = Util.formateDate(calendar.getTime(), "dd-MM-yyyy HH:mm");
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        String endOfDay = Util.formateDate(calendar.getTime(), "dd-MM-yyyy HH:mm");

        String[] params = {"Start_Time="+now, "End_Time="+endOfDay};
        try {
            return httpclient.performRequest(httpclient.buildRequest(HTTP.GET, "/reminders/get", params), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    // parseJSON does what it says, parses the API's JSON
    @Override
    public Reminder parseJson(JSON jsonD, int position) throws Exception {
        JSON json = jsonD.index(position);
        return new Reminder(
            json.key("Headers").key("Subject").stringValue(), json.key("Body").stringValue(),
            json.key("Tags").getJsonArray(), json.key("DateTime").stringValue());
    }




    @Override
    public RecyclerView.Adapter getAdapterInstance(List<Reminder> reminders) {
        return new ReminderAdapter(reminders, this.preferences);
    }
}