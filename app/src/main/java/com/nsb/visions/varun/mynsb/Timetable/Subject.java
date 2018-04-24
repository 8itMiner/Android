package com.nsb.visions.varun.mynsb.Timetable;

import android.util.Log;

/**
 */

public class Subject {

    public String className;
    public String room;
    public String teacher;
    public String period;
    public String time;

    public Subject(String className, String room, String teacher, String period, String time) {
        Log.d("subject-data", "D" + className);
        this.className = className;
        this.room = room;
        this.teacher = teacher;
        this.period = period;
        this.time = time;
    }

}
