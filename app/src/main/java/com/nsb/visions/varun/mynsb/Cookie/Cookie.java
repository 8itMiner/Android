package com.nsb.visions.varun.mynsb.Cookie;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by varun on 5/01/2018. Coz varun is awesome a popatato :)
 */


// Implementation of a permanent cookie store
public class Cookie implements CookieJar {

    // Containers
    private HashMap<String, List<okhttp3.Cookie>> cookiesMap;
    private SharedPreferences preferences;

    // Constructor
    public Cookie(@NonNull SharedPreferences preferences) {
        this.preferences = preferences;
    }


    // Methods that must be overridden
    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<okhttp3.Cookie> cookies) {
        // Attain the host name and push into hashmap
        cookiesMap.put(url.host().toString(), cookies);
    }

    @Override
    public List<okhttp3.Cookie> loadForRequest(@NonNull HttpUrl url) {
        if (cookiesMap != null) {
            // Get cookies for host from hashmap
            return cookiesMap.get(url.host().toString());
        }
        return null;
    }


    // Function to save user data into shared preferences
    public void save() throws Exception {
        // Get a string version of the cookies hashmap through serialization
        String serializedObject = "";
        // Start the real serialization methods
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream so = new ObjectOutputStream(bo);
        so.writeObject(this.cookiesMap);
        so.flush();
        serializedObject = bo.toString();

        // Save this to saved preferences
        preferences.edit().putString("mlg-cookie-store-blazit420-wrekt", serializedObject).apply();
    }


    public void loadCookies() throws Exception {
        // Deserialize the object from our preferences
        // Get the cookies
        String objectString = preferences.getString("mlg-cookie-store-blazit420-wrekt", "");
        // Deserialize it
        byte b[] = objectString.getBytes();
        ByteArrayInputStream bi = new ByteArrayInputStream(b);
        ObjectInputStream si = new ObjectInputStream(bi);
        // Get the actual object
        HashMap<String, List<okhttp3.Cookie>> obj = (HashMap<String, List<okhttp3.Cookie>>) si.readObject();
        // Save into our local map
        this.cookiesMap = obj;
        // All done
    }

}
