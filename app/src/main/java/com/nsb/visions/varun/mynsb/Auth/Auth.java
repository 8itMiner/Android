package com.nsb.visions.varun.mynsb.Auth;


import android.content.Context;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;
import com.nsb.visions.varun.mynsb.User.User;

import java.io.IOException;

import eu.amirs.JSON;
import okhttp3.Credentials;
import okhttp3.Response;

/**
 */


// Auth class for attaining user details or logging in
public class Auth {

    private HTTP httpClient;
    public Auth(Context context) {this.httpClient = new HTTP(context);}




    // auth takes a studentID and password and authenticates against the API, it then returns a User obj which contains all of the info of the current user
    public User auth(String studentID, String password) throws IOException, InterruptedException, HTTP.HTTPUserError, HTTP.HTTPError {
        // Clear all the cookies currently in storage
        httpClient.cookieJar.clear();

        Response loginResp = httpClient.performRequest(
            httpClient.buildRequest(HTTP.POST, "/user/auth", null)
                .addHeader("Authorization", Credentials.basic(studentID, password)), false);
        if (loginResp.code() != 200) {
            throw new HTTP.HTTPUserError("Could not log in");
        }
        // Return the details for the currently logged in user
        return this.getUserDetails();
    }




    // getUserDetails gets the details for the user currently logged into the API
    private User getUserDetails() throws HTTP.HTTPError, IOException, InterruptedException {
        Response userDataResp = httpClient.performRequest(
                httpClient.buildRequest(HTTP.GET, "/user/getdetails", null), false);
        User user;
        try {
            String body = userDataResp.body().string();
            JSON userData = new JSON(body);

            // Determine if the request was successful
            if (userDataResp.code() != 200) {
                throw new HTTP.HTTPError("Something went horrible wrong");
            }

            // Get the user data and parse it
            Integer StudentID = userData.key("Message").key("Body").index(0).key("StudentID").intValue();
            String Fname = userData.key("Message").key("Body").index(0).key("Fname").stringValue();
            String Lname = userData.key("Message").key("Body").index(0).key("Lname").stringValue();
            Integer Year = userData.key("Message").key("Body").index(0).key("Year").intValue();

            user = new User(StudentID, Fname, Lname, Year);
        } catch (Exception e) {
            throw new HTTP.HTTPError("Something went horribly wrong");
        }

        return user;
    }
}
