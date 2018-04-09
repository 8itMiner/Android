package com.nsb.visions.varun.mynsb.Jobs.NotificationDispatchers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.nsb.visions.varun.mynsb.Common.Loader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
    Created: 7/03/2018
 */

abstract public class NotificationDispatcher<Model> {

    // Model describes the class that corresponds to what data we are working with, e.g the timetable dispatcher would use the subject model

    protected Context context;
    protected SharedPreferences preferences;


    // Construct
    /*
        @params;
            Context context,
            SharedPreferences preference
     */
    public NotificationDispatcher(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;
    }


    // Schedule notification function
    @SuppressLint("SimpleDateFormat")
    public void scheduleNotifications() throws ParseException {
        // Create a new Timetables instance
        List<Model> models = getLoaderInstance().getModels();


        // Iterate over list and create a notification for each little subject
        for(Model model: models) {
            // Link that
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm pm");
            // Parse our time so we know when to show this notification
            Date displayDate = simpleDateFormat.parse(getNotificationTime(model));

            // Begin scheduling the notification
            scheduleNotification(model, displayDate);
        }
    }


    private void scheduleNotification(Model model, Date date) {
        HashMap<String, String> notificationDetails = getNotificationDetails(model);

        // TODO: FIll in code later
    }


    // Returns an instance of the loader
    abstract Loader<Model> getLoaderInstance();
    abstract String getNotificationTime(Model model);
    /*
        Scheme:
            "Title" -> notification title
            "Body" -> notification image
     */
    abstract HashMap<String, String> getNotificationDetails(Model model);





}
