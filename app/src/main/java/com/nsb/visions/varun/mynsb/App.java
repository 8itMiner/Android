package com.nsb.visions.varun.mynsb;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.evernote.android.job.JobManager;
import com.nsb.visions.varun.mynsb.Jobs.Dispatchers.JobDispatcher;

/**
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences preferences = getSharedPreferences("MyNSB", Context.MODE_PRIVATE);

        // Little piece of code used to start stuff like sync
        JobManager.create(this).addJobCreator(new JobDispatcher(getApplicationContext(), preferences));
    }

}
