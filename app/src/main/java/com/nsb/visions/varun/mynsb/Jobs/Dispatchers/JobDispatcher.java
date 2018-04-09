package com.nsb.visions.varun.mynsb.Jobs.Dispatchers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import com.nsb.visions.varun.mynsb.Jobs.TimetableSync;
import com.nsb.visions.varun.mynsb.Common.Util;

/**
 */


// Job dispatcher class determines what job to run given a specific tag
public class JobDispatcher implements JobCreator {


    private Context context;
    private SharedPreferences preferences;

    public JobDispatcher(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;
    }

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        // Determine if there is an internet connection, only then should we pull data from the API
        if (Util.isNetworkAvailable(this.context)) {
            switch (tag) {
                // Timetable sync job
                case TimetableSync.TAG:
                    return new TimetableSync(context, preferences);
            }
        }
        return null;
    }
}
