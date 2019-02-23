package com.nsb.visions.varun.mynsb;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.nsb.visions.varun.mynsb.Jobs.JobCreator;
import com.nsb.visions.varun.mynsb.Jobs.ReminderScheduler;
import com.nsb.visions.varun.mynsb.Jobs.TimetableScheduler;

/**
 */

// App class is the instance that starts off the app and handles pre launch activities
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        JobManager.create(this).addJobCreator(new JobCreator());
        TimetableScheduler.schedule();
        ReminderScheduler.schedule();
    }

}
