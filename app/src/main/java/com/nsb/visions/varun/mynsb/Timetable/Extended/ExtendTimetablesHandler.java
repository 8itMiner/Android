package com.nsb.visions.varun.mynsb.Timetable.Extended;

import android.content.Context;
import android.content.Intent;

import com.nsb.visions.varun.mynsb.ExpandedTimetables;

/**
 * Created by varun on 25/04/2018. Coz varun is awesome as hell :)
 */

public class ExtendTimetablesHandler {
    public static void handleExtendTimetablesButton(Context context) {
        Intent move = new Intent(context, ExpandedTimetables.class);
        context.startActivity(move);
    }
}
