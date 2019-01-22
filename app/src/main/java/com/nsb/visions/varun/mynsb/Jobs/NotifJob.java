package com.nsb.visions.varun.mynsb.Jobs;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.nsb.visions.varun.mynsb.App;
import com.nsb.visions.varun.mynsb.R;

import java.util.Date;
import java.util.Random;

/**
 * Created by varun on 22/01/2019. Coz varun is awesome as hell :)
 */

public class NotifJob extends Job {

    static final String TAG = "mynsb-NotifJob";


    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        PendingIntent notifIntent = PendingIntent.getActivity(getContext(), 0,
            new Intent(getContext(), App.class), 0);

        Notification notification = new NotificationCompat.Builder(getContext())
            .setContentTitle(params.getExtras().getString("notificationTitle", ""))
            .setContentText(params.getExtras().getString("notificationText", ""))
            .setAutoCancel(true)
            .setContentIntent(notifIntent)
            .setSmallIcon((params.getExtras().getInt("smallIcon", R.drawable.cast_ic_notification_1)))
            .setShowWhen(true)
            .setLocalOnly(true)
            .build();

        NotificationManagerCompat.from(getContext())
            .notify(new Random().nextInt(), notification);


        return Result.SUCCESS;
    }


    static void schedule(Date expected, com.nsb.visions.varun.mynsb.Notifications.Notification notification) {
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString("notificationTitle", notification.notificationTitle);
        extras.putString("notificationText", notification.notificationText);
        extras.putInt("smallIcon", notification.smallIcon);


        new JobRequest.Builder(NotifJob.TAG)
            .setExact(expected.getTime())
            .setUpdateCurrent(true)
            .setExtras(new PersistableBundleCompat(extras))
            .build()
            .schedule();
    }
}
