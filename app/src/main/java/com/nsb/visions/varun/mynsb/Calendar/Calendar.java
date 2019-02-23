package com.nsb.visions.varun.mynsb.Calendar;

import com.nsb.visions.varun.mynsb.Common.Util;

/**
 */

// The calendar class represents a simple view of a calendar entry, it contains the basic data required for the calendar MVC RULES!!!!
public class Calendar {

    public String name;
    public String time;

    // Simple constructor
    public Calendar(String name, String time) {
        this.name = name;
        this.time = Util.formateDate(Util.parseDate(time, "yyyy-MM-dd"), "dd-MM-yyyy");
    }

}
