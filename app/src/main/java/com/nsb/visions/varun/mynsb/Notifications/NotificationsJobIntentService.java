package com.nsb.visions.varun.mynsb.Notifications;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

/**
 * Created by varun on 13/02/2018. Coz varun is awesome as hell :)
 */

public class NotificationsJobIntentService extends JobIntentService {

    // Unique job id for the service
    private static final int JOB_ID = 3421;

    public static void enqueueWork(Context ctx, Intent intent) {
        enqueueWork(ctx, NotificationsJobIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }
}
