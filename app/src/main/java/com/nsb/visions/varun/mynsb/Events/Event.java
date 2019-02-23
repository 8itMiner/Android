package com.nsb.visions.varun.mynsb.Events;

import com.nsb.visions.varun.mynsb.Common.Util;

import java.util.Date;

/**
 * Created by varun on 21/01/2018.
 */

class Event {

    String eventName;
    Date eventStart;
    Date eventEnd;
    String eventLocation;
    String eventOrganiser;
    String eventShortDesc;
    String eventLongDesc;
    String eventPictureUrl;

    Event(String eventName, String eventStart, String eventEnd, String eventLocation, String eventOrganiser, String eventShortDesc, String eventLongDesc, String eventPictureUrl) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventOrganiser = eventOrganiser;
        this.eventShortDesc = eventShortDesc;
        this.eventLongDesc = eventLongDesc;
        this.eventPictureUrl = eventPictureUrl;
        this.eventStart = Util.parseDate(eventStart, "yyyy-MM-dd'T'hh:mm:ss'Z'");
        this.eventEnd = Util.parseDate(eventEnd, "yyyy-MM-dd'T'hh:mm:ss'Z'");
    }
}
