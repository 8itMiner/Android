package com.nsb.visions.varun.mynsb.HTTP;

import android.content.Context;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by varun on 5/01/2018. Coz varun is awesome as hell :)
 */

// HTTP class that uses stuff such as cookie managers and stuff and makes http requests easier
public class HTTP {

    // Cookie manager
    private OkHttpClient client;
    private Context context;



    public HTTP(Context context) {
        this.context = context;

        // Setup the cookie jar
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this.context));

        // Set up the okhttp client
        this.client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
    }

    public Response performRequest(Request request) throws Exception {
        // Perform the request
        Response response = client.newCall(request).execute();
        // Save the cookies
        return response;
    }
}
