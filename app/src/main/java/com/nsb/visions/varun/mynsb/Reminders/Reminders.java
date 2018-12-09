package com.nsb.visions.varun.mynsb.Reminders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.FourU.ArticleAdapter;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import eu.amirs.JSON;
import okhttp3.CacheControl;
import okhttp3.Request;
import okhttp3.Response;

/**
 */


//  Reminders loads all reminders for the specific user for *_today_*
public class Reminders extends Loader<Reminder>{

    private SharedPreferences preferences;

    public Reminders(Context context, SharedPreferences preferences) {
        super(context, Reminders.class);
        this.preferences = preferences;
    }


    @Override
    public Response sendRequest() {
        // Set up the http client
        HTTP httpclient = new HTTP(this.context);
        // Prevent caching
        CacheControl cacheControl = CacheControl.FORCE_NETWORK;


        // Start up a request to be sent to the api
        Request getReminders = new Request.Builder()
            .url("http://35.189.50.185:8080/api/v1/reminders/Get/Today")
            .get()
            .cacheControl(cacheControl)
            .build();
        // Send the request

        try {
            return httpclient.performRequest(getReminders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public Reminder parseJson(JSON jsonD, int position) throws Exception {

        JSON json = jsonD.index(position);
        return new Reminder(
            json.key("Headers").key("Subject").stringValue(), json.key("Body").stringValue(),
            json.key("Tags").getJsonArray(), json.key("ReminderDateTime").stringValue());
    }


    @Override
    public RecyclerView.Adapter getAdapterInstance(List<Reminder> reminders) {
        return new ReminderAdapter(reminders, this.preferences);
    }
}
