package com.nsb.visions.varun.mynsb;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nsb.visions.varun.mynsb.Auth.Auth;
import com.nsb.visions.varun.mynsb.User.User;

import java.util.HashMap;
import java.util.Locale;


public class SignIn extends AppCompatActivity {

    // UI updater handler
    private final Handler uiHandler = new Handler();
    // Authenticator for authentication
    private final Auth authenticator = new Auth(getApplicationContext());
    // Shared preferences
    private final SharedPreferences sharePref = getSharedPreferences("MyNSB", Context.MODE_PRIVATE);
    // Shared preferences editor
    private final SharedPreferences.Editor editor = sharePref.edit();
    // Asset manager for loading various assets
    private final AssetManager am = getApplicationContext().getAssets();
    // Raleway font used regularly throughout page
    private final Typeface raleway = Typeface.createFromAsset(am,
        String.format(Locale.US, "fonts/%s", "raleway_regular.ttf"));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Determine what to do with the incoming user through the router user function
        routeUser(sharePref, editor);
        // Load in the layout after shared pref has been checked
        setContentView(R.layout.activity_sign_in);



        // Attain all the required ui components
        final RelativeLayout loaderView = (RelativeLayout) findViewById(R.id.loaderContainer);
        final RelativeLayout mainContent = (RelativeLayout) findViewById(R.id.activity_sign_in);
        // Title text displays: "Sign In"
        final TextView titleText = ((TextView) findViewById(R.id.signInText));
        Button signInButton = (Button) findViewById(R.id.signInButton);
        // Initialize the UI
        initUI(titleText);




        // Set an onclick listener for the submit button
        signInButton.setOnClickListener(v -> {
            // Get the values from the editext fields
            HashMap<String, String> enteredDetails = getEnteredDetails();

            // Do a quick null check
            if (enteredDetails == null) {
                return;
            }

            // Open the loading animation
            // Set the background to not be clickable
            mainContent.setClickable(false);
            // Animate the loader into view
            animateLoader(loaderView, true);

            // Thread for authentication...
            Thread auth = new Thread(() -> {
                HashMap<String, Object> authDetails = performAuth(enteredDetails);

                if ((Boolean) authDetails.get("success")) { // One line heros :p, pre much it just gets the valid flag from the validUser function {Success}
                    // Update the shared preferences
                    updatePrefDetails(editor, (User) authDetails.get("user"));
                    // Redirect the user to the home page
                    Intent moveUser = new Intent(SignIn.this, Home.class);
                    startActivity(moveUser);
                    finish();
                } else { // Failure
                    // Fade out the loader view
                    animateLoader(loaderView, false);
                    // Set the background to be clickable
                    mainContent.setClickable(true);
                    // Alert the user that the authentication attempt was unsuccessful
                    Toast.makeText(SignIn.this, "Details are invalid", Toast.LENGTH_LONG).show();
                }

            });
            auth.start();

        });

        // End of main function ======================================
    }






    // Beginning of utility functions =======================================


    /* validUser returns a hashmap containing the user details and a flag which tell us if the auth was a success
        @params;
            HashMap<String, String> enteredDetails
     */
    private HashMap<String, Object> performAuth(HashMap<String, String> enteredDetails) {
        // Init the hashmap to return
        HashMap<String, Object> toReturn = new HashMap<>();
        try {
            User user = authenticator.auth(enteredDetails.get("studentID"), enteredDetails.get("password"));
            // Update the shared preferences with the details we have been given
            toReturn.put("success", true);
            toReturn.put("user", user);
        } catch (Exception _) {
            toReturn.put("success", false);
        }

        return toReturn;
    }





    /* getEnteredDetails returns a map with the currently entered username and password returns null if the details are empty
        @params;
            nil

     */
    @Nullable
    private HashMap<String, String> getEnteredDetails() {
        // Attain the details
        final String studentID = ((EditText) findViewById(R.id.studentID)).getText().toString();
        final String password = ((EditText) findViewById(R.id.studentPassword)).getText().toString();

        if (studentID.isEmpty() || password.isEmpty()) {
            // Post onto the main UI thread
            uiHandler.post(() -> {
                Toast.makeText(SignIn.this, "Student ID or Password is empty", Toast.LENGTH_LONG).show();
            });
            return null;
        }

        // Push the user's details into the hashmap
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("studentID", studentID);
        userMap.put("password", password);

        // Return the final map
        return userMap;
    }






    /* initUI initialises the ui by setting the fonts and anything else u might want to happen
        @params;
            TextView signInText
     */
    private void initUI(TextView signInText) {
        signInText.setTypeface(raleway);
    }





    /* routeUser routes the current user based on the stored shared preferences, if this is the first one a tutorial is shown if they are logged in they are taken to the home screen
        @params;
            SharedPreferences preferences
            SharedPreferences.Editor editor
        ! WARNING
            Incomplete method, please see TODO for more details
     */
    private void routeUser(SharedPreferences preferences, SharedPreferences.Editor editor) {
        // Determine if this is the first time they have run the app if so then take them to the tutorial
        if (preferences.getBoolean("firstrun", true)) {
            // Set the logged-in flag to false
            editor.putBoolean("logged-in", false);
            // Set the firstrun flag to false
            editor.putBoolean("firstrun", false);
            // Apply changes
            editor.apply();
            // Show the tutorial
            // TODO: Once the tutorial is completed implement this redirect

        // Determine if the user is logged-in through the flag
        } else if (preferences.getBoolean("logged-in", false)) {
            // Redirect the user to the home page
            Intent redirect = new Intent(SignIn.this, Home.class);
            startActivity(redirect);
        }
    }





    /* updatePrefDetails updates the shared preferences given a specific user
        @params;
            SharedPreferences.Editor editor
            User user
     */
    private void updatePrefDetails(SharedPreferences.Editor editor, User user) {
        // Serialize the user data and put the string in the editor
        editor.putString("user-data", user.toString());
        // Set the logged in data
        editor.putBoolean("logged-in", true);
        // Apply our changes
        editor.apply();
    }






    /* animateLoader takes the loader view and displays it to the user using a face in animation
        @params;
            RelativeLayout loadingScreen
            Boolean openClose:
                        True: open,
                        False: close

     */
    private void animateLoader(RelativeLayout loadingScreen, Boolean openClose) {
        // Declare animations, one for fading in the other for fading out
        // Fade out
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(150);
        fadeOut.setStartOffset(150);
        fadeOut.setInterpolator(new AccelerateInterpolator());

        // Fade in
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(200);


        // Post the fade animation to a runnable
        uiHandler.post(() -> {
            if (openClose) { // Open the loading screen
                loadingScreen.setVisibility(View.VISIBLE);
                loadingScreen.startAnimation(fadeIn);
            } else { // Close the loading screen
                loadingScreen.startAnimation(fadeOut);
                loadingScreen.setVisibility(View.GONE);
            }
        });
    }
}