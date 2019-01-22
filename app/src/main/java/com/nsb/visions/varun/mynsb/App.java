package com.nsb.visions.varun.mynsb;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.nsb.visions.varun.mynsb.Jobs.JobCreator;
import com.nsb.visions.varun.mynsb.Jobs.TimetableScheduler;

/**
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        JobManager.create(this).addJobCreator(new JobCreator());
        TimetableScheduler.schedule();
    }

}
