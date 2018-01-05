package com.nsb.visions.varun.mynsb.Auth;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import eu.amirs.JSON;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.JavaNetCookieJar;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.nsb.visions.varun.mynsb.User.User;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;

/**
 * Created by varun on 30/12/2017. Coz varun is awesome as hell :)
 */


// Auth class for attaining user details or logging in
public class Auth {





    // Auth function for quick authentication
    public User auth(String studentID, String password, SharedPreferences preferences) throws RuntimeException, IOException {


        // init OkHttpClient
        OkHttpClient client = new OkHttpClient();


        // Set up the request
        Request login = new Request.Builder()
                .url("http://35.189.45.152:8080/api/v1/user/Auth")
                .method("POST", RequestBody.create(null, new byte[0]))
                .addHeader("Authorization", Credentials.basic(studentID, password))
                .build();




        // Retrieve the response
        Response loginResp = client.newCall(login).execute();
        // Get the set cookie header from the request
        
        if (loginResp.code() != 200) {
            // Throw the error meaning that the request was unsuccessful
            throw new RuntimeException("User details are invalid");
        }


        return this.getUserDetails(preferences);
    }





    // Function to get user details
    public User getUserDetails(SharedPreferences preferences) throws RuntimeException, IOException {

        // Create the cookie helper
        // init cookie manager
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(preferences));

        // init OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        // Get user data and return it
        Request getUserDetails = new Request.Builder()
                .url("http://35.189.45.152:8080/api/v1/user/GetDetails")
                .build();
        Response userDataResp = client.newCall(getUserDetails).execute();
        // Read Body
        String body = userDataResp.body().string();

        User user;

        // Begin parsing json
        JSON userData = new JSON(body);
        try {

            // Determine if the request was successful
            if (userDataResp.code() != 200) {
                throw new RuntimeException("Something went horrible wrong");
            }

            Integer StudentID = userData.key("Message").key("Body").index(0).key("StudentID").intValue();
            String Fname = userData.key("Message").key("Body").index(0).key("Fname").stringValue();
            String Lname = userData.key("Message").key("Body").index(0).key("Lname").stringValue();
            Integer Year = userData.key("Message").key("Body").index(0).key("Year").intValue();
            // Push into user container
            user = new User(StudentID, Fname, Lname, Year);
        } catch (RuntimeException e) {
            throw new RuntimeException("Something went horribly wrong");
        }

        return user;
    }








}
