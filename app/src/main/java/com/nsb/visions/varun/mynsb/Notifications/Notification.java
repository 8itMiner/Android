package com.nsb.visions.varun.mynsb.Notifications;

import android.app.PendingIntent;

/**
 * Created by varun on 20/01/2019.
 */

public class Notification {

    public static final String REMINDER_NOTIF_CHANNEL = "reminder-notif-channel";
    public static final String TIMETABLE_NOTIF_CHANNEL = "timetable-notif-channel";

    public static final String REMINDER_NOTIF_CHANNEL_NAME = "MyNSB Reminders";
    public static final String TIMETABLE_NOTIF_CHANNEL_NAME = "MyNSB Timetable Notifications";

    public int smallIcon;
    public String notificationTitle;
    public String notificationText;


    public Notification(int smallIcon, String notificationTitle, String notificationText) {
        this.smallIcon = smallIcon;
        this.notificationTitle = notificationTitle;
        this.notificationText = notificationText;
    }
}
