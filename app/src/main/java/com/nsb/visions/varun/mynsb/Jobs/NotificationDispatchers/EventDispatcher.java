package com.nsb.visions.varun.mynsb.Jobs.NotificationDispatchers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Events.Event;
import com.nsb.visions.varun.mynsb.Events.Events;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 */

public class EventDispatcher extends NotificationDispatcher<Event> {


    public EventDispatcher(Context context, SharedPreferences preferences) {
        super(context, preferences);
    }

    @Override
    Loader<Event> getLoaderInstance() {
        return new Events(this.context);
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    String getNotificationTime(Event event) {
        // Init a simpledateformat object only used to format our date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm pm");

        return simpleDateFormat.format(event.eventStart);
    }

    @Override
    HashMap<String, String> getNotificationDetails(Event event) {
        // Init a hashmap
        HashMap<String, String> notificationDetails = new HashMap<>();
        notificationDetails.put("Title", event.eventName);
        notificationDetails.put("Body", event.eventShortDesc);

        return notificationDetails;
    }
}
