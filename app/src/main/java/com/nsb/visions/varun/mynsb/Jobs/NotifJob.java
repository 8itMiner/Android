package com.nsb.visions.varun.mynsb.Jobs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.nsb.visions.varun.mynsb.App;
import com.nsb.visions.varun.mynsb.Notifications.Notification;
import com.nsb.visions.varun.mynsb.R;

import java.util.Date;
import java.util.Random;

/**
 * Created by varun on 22/01/2019.
 */

public class NotifJob extends Job {

    static final String TAG = "mynsb-NotifJob";


    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Intent resultIntent = new Intent(getContext(), App.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent notifIntent = PendingIntent.getActivity(getContext(), 0, resultIntent, 0);

        final String NOTIF_CHANNEL_ID = params.getExtras().getString("notificationChannelID", "MyNSB_Notifications");
        final String NOTIF_CHANNEL_NAME = params.getExtras().getString("notificationChannelName", "MyNSB - Notifications");

        // Build notif
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getContext(), NOTIF_CHANNEL_ID)
            .setContentTitle(params.getExtras().getString("notificationTitle", "MyNSB"))
            .setContentText(params.getExtras().getString("notificationText", "Hello From myNSB"))
            .setAutoCancel(true)
            .setContentIntent(notifIntent)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setColor(getContext().getResources().getColor(R.color.colorPrimary))
            .setSmallIcon((params.getExtras().getInt("smallIcon", R.drawable.mynsb_notification_logo)))
            .setShowWhen(true)
            .setLocalOnly(true);

        // Create notification manager
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Android OREO plus
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notifChannel = new NotificationChannel(NOTIF_CHANNEL_ID,
                NOTIF_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notifChannel.enableLights(true);
            notifChannel.setLightColor(Color.MAGENTA);
            notifChannel.enableVibration(true);
            notifChannel.setVibrationPattern(new long[]{0, 100, 200, 100, 0, 100, 200, 100, 0});

            // Create the channel
            notification.setChannelId(NOTIF_CHANNEL_ID);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notifChannel);
        }


        Random random = new Random();
        // Send the notification
        assert notificationManager != null;
        notificationManager.notify(random.nextInt(), notification.build());

        return Result.SUCCESS;
    }



    static public void schedule(Date expected, Notification notification, String channelID, String channelName) {
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString("notificationTitle", notification.notificationTitle);
        extras.putString("notificationText", notification.notificationText);
        extras.putString("notificationChannelID", channelID);
        extras.putString("notificationChannelName", channelName);
        extras.putInt("smallIcon", notification.smallIcon);

        long executionTime = expected.getTime() - System.currentTimeMillis();

        new JobRequest.Builder(NotifJob.TAG)
            .setExact(executionTime)
            .setExtras(new PersistableBundleCompat(extras))
            .build()
            .schedule();
    }

}
