package com.nsb.visions.varun.mynsb.Jobs.NotificationDispatchers;

import android.content.Context;
import android.content.SharedPreferences;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Timetable.Subject;
import com.nsb.visions.varun.mynsb.Timetable.Timetables;

import java.util.HashMap;

/**
 */

public class TimetableDispatcher extends NotificationDispatcher<Subject> {


    public TimetableDispatcher(Context context, SharedPreferences preferences) {
        super(context, preferences);
    }



    // OVERRIDDEN METHODS

    @Override
    Loader getLoaderInstance() {
        return new Timetables(this.context, this.preferences, false);
    }

    @Override
    String getNotificationTime(Subject subject) {
        return subject.time;
    }

    @Override
    HashMap<String, String> getNotificationDetails(Subject subject) {
        HashMap<String, String> details = new HashMap<>();
        details.put("Title", subject.className);
        details.put("Body", subject.teacher + " " + subject.room);
        return details;
    }





}
