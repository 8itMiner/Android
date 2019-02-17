package com.nsb.visions.varun.mynsb;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TimingLogger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nsb.visions.varun.mynsb.Auth.Auth;
import com.nsb.visions.varun.mynsb.User.User;

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



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // We need to time this
        TimingLogger logger = new TimingLogger("timer timer", "method A");
        logger.addSplit("Start route");
        // Setup the things that require context
        this.sharePref = getSharedPreferences("MyNSB", Context.MODE_PRIVATE);
        this.editor = sharePref.edit();

        this.am = getApplicationContext().getAssets();
        this.raleway = Typeface.createFromAsset(am,
            String.format(Locale.US, "fonts/%s", "raleway_regular.ttf"));
        // Determine what to do with the incoming user through the router user function
        // Load in the layout after shared pref has been checked

        // Setup authenticator
        this.authenticator = new Auth(getApplicationContext());



        // Attain all the required ui components
        final RelativeLayout loaderView = findViewById(R.id.loaderContainer);
        final RelativeLayout mainContent = findViewById(R.id.activity_sign_in);
        // Title text displays: "Sign In"
        final TextView titleText = findViewById(R.id.signInText);
        Button signInButton = findViewById(R.id.signInButton);
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
                try {
                    // Get user details
                    HashMap<String, Object> authDetails = performAuth(enteredDetails);

                    // Update the shared preferences
                    updatePrefDetails(editor, (User) authDetails.get("user"));
                    // Redirect the user to the home page
                    Intent moveUser = new Intent(SignIn.this, Home.class);
                    startActivity(moveUser);
                    finish();
                } catch (Exception e) {
                    // Fade out the loader view
                    showLoader(alertDialog, false);

                    uiHandler.post(() -> {
                        // Set the background to be clickable
                        mainContent.setClickable(true);
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
    private HashMap<String, Object> performAuth(HashMap<String, String> enteredDetails) throws Exception{
        // Init the hashmap to return
        HashMap<String, Object> toReturn = new HashMap<>();
        try {
            User user = authenticator.auth(enteredDetails.get("studentID"), enteredDetails.get("password"));
            // Update the shared preferences with the details we have been given
            toReturn.put("success", true);
            toReturn.put("user", user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("User details were invalid");
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