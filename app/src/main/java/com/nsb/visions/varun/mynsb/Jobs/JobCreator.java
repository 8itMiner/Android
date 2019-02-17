package com.nsb.visions.varun.mynsb.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;

/**
 * Created by varun on 22/01/2019. Coz varun is awesome as hell :)
 */

public class JobCreator implements com.evernote.android.job.JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case NotifJob.TAG:
                return new NotifJob();
            case TimetableScheduler.TAG:
                return new TimetableScheduler();
            case ReminderScheduler.TAG:
                return new ReminderScheduler();
            default:
                return null;
        }
    }
}
