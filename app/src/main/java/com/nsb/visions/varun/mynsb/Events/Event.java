package com.nsb.visions.varun.mynsb.Events;

import android.annotation.SuppressLint;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by varun on 21/01/2018. Coz varun is awesome as hell :)
 */

public class Event {

    public Integer eventID;
    public String eventName;
    public Date eventStart;
    public Date eventEnd;
    public String eventLocation;
    public String eventOrganiser;
    public String eventShortDesc;
    public String eventPictureUrl;

    public Event(int eventId, String eventName, String eventStart, String eventEnd, String eventLocation,
                 String eventOrganiser, String eventShortDesc, String eventPictureUrl) throws Exception {

        this.eventID = eventId;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventOrganiser = eventOrganiser;
        this.eventShortDesc = eventShortDesc;
        this.eventPictureUrl = eventPictureUrl;

        // Convert the date strings to actual dates

        // Setup a simple date format
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        this.eventStart = simpleDateFormat.parse(eventStart);
        this.eventEnd = simpleDateFormat.parse(eventEnd);

    }
}
