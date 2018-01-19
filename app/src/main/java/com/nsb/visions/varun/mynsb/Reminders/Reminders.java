package com.nsb.visions.varun.mynsb.Reminders;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;

import java.util.ArrayList;
import java.util.List;

import eu.amirs.JSON;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by varun on 17/01/2018. Coz varun is awesome as hell :)
 */


//  Reminders loads all reminders for the specific user for *_today_*
public class Reminders {


    // static function loadReminders
    static void loadReminders(RecyclerView rv, TextView errorHolder, Context context, Handler uiHandler) {

    }



    static void loadAdapter(RecyclerView recyclerView, TextView errorHolder, List<Reminder> reminders, Handler uiHandler) throws Exception{
        // Push ui updates into the handler
        uiHandler.post(() -> {
            // Determine if reminders are empty and if they are then
            if (reminders.isEmpty()) {
                // Display error message
                recyclerView.setVisibility(View.GONE);
                errorHolder.setVisibility(View.VISIBLE);
            }

            
        });
    }

    // function getReminders
    static List<Reminder> getReminders(Context context) throws Exception {
        List<Reminder> reminderList = new ArrayList<>();
        // Set up the http client with the context that we have
        HTTP httpclient = new HTTP(context);

        // Start up a request to be sent to the api
        Request getReminders = new Request.Builder()
            .url("http://35.189.45.152:8080/api/v1/reminders/Get/Today")
            .get()
            .build();

        // Get the client to perform the request
        Response response = httpclient.performRequest(getReminders);

        String rawJson = response.body().string();

        // Begin reading the response's json and forming a list of reminders
        JSON json = new JSON(rawJson);

        // Retrieve the actual reminders
        JSON reminders = json.key("Message").key("Body").index(0);

        // Iterate over reminders to retrieve the actual reminders
        for (int i = 0; i < reminders.count(); i++) {
            Reminder reminder = new Reminder(
                reminders.index(i).key("Headers").key("Subject").stringValue(), reminders.index(i).key("Body").stringValue(),
                reminders.index(i).key("tags").getJsonArray(), reminders.index(i).key("ReminderDateTime").stringValue());

            // Push this reminder into our global list and return it at the end
            reminders.add(reminder);
        }

        return reminderList;
    }

    // function load adapter

}
