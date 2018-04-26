package com.nsb.visions.varun.mynsb.Jobs;

import android.app.Notification;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;

import java.sql.Date;

/**
 * Created by varun on 24/04/2018. Coz varun is awesome as hell :)
 */

// This class just schedules our little jobs
public class Alarm extends Job {

    private Notification notification;

    public Alarm(Notification notification, Date time) {
        this.notification = notification;
    }


    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        return null;
    }
}
