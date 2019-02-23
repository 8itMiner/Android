package com.nsb.visions.varun.mynsb.Common;

import android.app.Application;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;

import com.nsb.visions.varun.mynsb.Reminders.Create.CreateReminderHandler;
import com.nsb.visions.varun.mynsb.Reminders.Reminders;
import com.nsb.visions.varun.mynsb.Timetable.Extended.ExtendTimetablesHandler;
import com.nsb.visions.varun.mynsb.Timetable.Timetables;

/**
 * Created by varun on 25/04/2018.
 */


// Class handles when a FAB is clicked and what to do with it when it is
public class HandleFABClicks {

    // switchHandler takes a FAB button and performs the appropriate action for that FAB button based off the class that is calling the function
    public static void switchHandler(Loader loader, FloatingActionButton fab, LayoutInflater inflater, Context context, Application application) {
        // Timetables
        if (loader.getClass() == Timetables.class) {
            // Pass it to the expanded timetables handler button
            ExtendTimetablesHandler.handleExtendTimetablesButton(context);
        // Reminders
        } else if (loader.getClass() == Reminders.class) {
            // Pass it to the create reminder handler button
            CreateReminderHandler.handleCreateReminderButton(fab, inflater, context, application);
        }
    }




}
