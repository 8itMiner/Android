package com.nsb.visions.varun.mynsb.Firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by varun on 23/01/2019.
 */

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {Log.d("FCM-TOKEN: ", token);}

}
