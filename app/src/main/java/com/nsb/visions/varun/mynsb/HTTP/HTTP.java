package com.nsb.visions.varun.mynsb.HTTP;

import android.content.SharedPreferences;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by varun on 5/01/2018. Coz varun is awesome as hell :)
 */

// HTTP class that uses stuff such as cookie managers and stuff and makes http requests easier
public class HTTP {

    // Cookie manager
    OkHttpClient client;
    SharedPreferences preferences;



    public HTTP(SharedPreferences preferences) {
        this.preferences = preferences;

        // Set up the okhttp client
        this.client = new OkHttpClient.Builder()
                .build();
    }

    public Response performRequest(Request request) throws Exception {
        // Perform the request
        Response response = client.newCall(request).execute();
        // Save the cookies
        return response;
    }
}
