package com.nsb.visions.varun.mynsb.Notifications;

import android.app.PendingIntent;

/**
 * Created by varun on 20/01/2019.
 */

public class Notification {
    public int smallIcon;
    public String notificationTitle;
    public String notificationText;


    public Notification(int smallIcon, String notificationTitle, String notificationText) {
        this.smallIcon = smallIcon;
        this.notificationTitle = notificationTitle;
        this.notificationText = notificationText;
    }
}
