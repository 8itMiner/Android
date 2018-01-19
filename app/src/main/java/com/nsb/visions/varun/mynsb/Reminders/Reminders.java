package com.nsb.visions.varun.mynsb.Reminders;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.FourU.ArticleAdapter;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.ArrayList;
import java.util.List;

import eu.amirs.JSON;
import okhttp3.Request;
import okhttp3.Response;

/**
 */


//  Reminders loads all reminders for the specific user for *_today_*
public class Reminders extends Loader<Reminder>{


    public Reminders(Context context) {
        super(context);
    }


    @Override
    public Response sendRequest() throws Exception {
        // Set up the http client
        HTTP httpclient = new HTTP(this.context);

        // Start up a request to be sent to the api
        Request getReminders = new Request.Builder()
            .url("http://35.189.45.152:8080/api/v1/reminders/Get/Today")
            .get()
            .build();

        // Send the request
        return httpclient.performRequest(getReminders);
    }



    @Override
    public Reminder parseJson(JSON json) throws Exception {
        return new Reminder(
            json.key("Headers").key("Subject").stringValue(), json.key("Body").stringValue(),
            json.key("tags").getJsonArray(), json.key("ReminderDateTime").stringValue());
    }


    // TODO: FIll in this when the reminder adapter has been written
    @Override
    public void getAdapterInstance(List<Reminder> reminders) {
        this.adapter = new ReminderAdapter(reminders);
    }


    // function load adapter

}
