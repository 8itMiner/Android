package com.nsb.visions.varun.mynsb;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nsb.visions.varun.mynsb.Auth.Auth;
import com.nsb.visions.varun.mynsb.User.User;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;


public class SignIn extends AppCompatActivity {

    // UI updater handler
    private final Handler uiHandler = new Handler();
    // Authenticator for authentication
    private Auth authenticator;
    // Shared preferences
    private SharedPreferences sharePref;
    // Shared preferences editor
    private SharedPreferences.Editor editor;
    // Asset manager for loading various assets
    private AssetManager am;
    // Raleway font used regularly throughout page
    private Typeface raleway;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup the things that require context
        this.sharePref = getSharedPreferences("MyNSB", Context.MODE_PRIVATE);
        this.editor = sharePref.edit();
        this.am = getApplicationContext().getAssets();
        this.raleway = Typeface.createFromAsset(am,
            String.format(Locale.US, "fonts/%s", "raleway_regular.ttf"));
        // Determine what to do with the incoming user through the router user function
        routeUser(sharePref, editor);
        // Load in the layout after shared pref has been checked
        setContentView(R.layout.activity_sign_in);

        // Setup authenticator
        this.authenticator = new Auth(getApplicationContext());



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
            // Create a dialog from the loaderview content
            // Setup a dialog
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignIn.this)
                .setCancelable(false);
            // Get the views
            LayoutInflater inflater = this.getLayoutInflater();
            View createView = inflater.inflate(R.layout.sign_in_loader, null);
            dialogBuilder.setView(createView);
            AlertDialog alertDialog = dialogBuilder.create();

            // Show the loader
            showLoader(alertDialog, true);

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
                    showLoader(alertDialog, false);

                    // Set the background to be clickable
                    mainContent.setClickable(true);
                    uiHandler.post(() -> {
                        // Alert the user that the authentication attempt was unsuccessful
                        Toast.makeText(SignIn.this, "Details are invalid", Toast.LENGTH_LONG).show();
                    });
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
        } catch (Exception e) {
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
        // Setup gson
        Gson gson = new Gson();

        // Serialize the user data and put the string in the editor
        editor.putString("user-data", user.toString());
        // Set the logged in data
        editor.putBoolean("logged-in", true);
        // CreateReminder a hashmap to represent reminder tag colours
        HashMap<String, String> reminderColours = new HashMap<>();
        // Push the minor stuff
        reminderColours.put("hw", "#64dd17");
        reminderColours.put("general", "#008cff");
        // Convert at the end once the data has been pushed
        String hashMapString = gson.toJson(reminderColours);


        // Push this into the shared prefs
        editor.putString("tag-colours", hashMapString);
        // Apply our changes
        editor.apply();
    }


    /* showLoader displays the the user the loader box
            @params;
                boolean load
                    If load = true then the loader will be loaded otherwise if it is faul then the loader will be hidden
                AlertDialog dialog
     */
    private void showLoader(AlertDialog dialog, boolean load) {
        if (load) {
            // Load the window views into the layout
            // Make the dialog fill the screen
            Window window = dialog.getWindow();
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            // Show the dialog
            dialog.show();
        } else {
            dialog.cancel();
        }
    }
}