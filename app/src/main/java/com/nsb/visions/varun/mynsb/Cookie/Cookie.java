package com.nsb.visions.varun.mynsb.Cookie;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.nsb.visions.varun.mynsb.Cookie.SerializableCookie;

import java.util.HashMap;
import java.util.List;

import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by varun on 8/01/2018. Coz varun is awesome as hell :)
 */

// Cookie manager class to handle directly with okhttp cookies
public class Cookie implements CookieJar {

    // Shared preferences for storing cookies
    private SharedPreferences preferences;


    public Cookie(@NonNull SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<okhttp3.Cookie> cookies) {

    }

    @Override
    public List<okhttp3.Cookie> loadForRequest(HttpUrl url) {
        return null;
    }
}
