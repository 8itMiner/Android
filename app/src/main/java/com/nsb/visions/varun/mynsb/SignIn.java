package com.nsb.visions.varun.mynsb;


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


// SignIN activity is that base view that people see when they are gonna sign in
public class SignIn extends AppCompatActivity {

    private final Handler uiHandler = new Handler();
    private Auth authenticator;
    private SharedPreferences.Editor editor;
    private Typeface raleway;


    // Activity entry point
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SharedPreferences sharePref = getSharedPreferences("MyNSB", Context.MODE_PRIVATE);
        this.editor = sharePref.edit();
        AssetManager am = getApplicationContext().getAssets();
        this.raleway = Typeface.createFromAsset(am,
            String.format(Locale.US, "fonts/%s", "raleway_regular.ttf"));

        this.authenticator = new Auth(getApplicationContext());

        // Attain all the required ui components
        final RelativeLayout loaderView = findViewById(R.id.loaderContainer);
        final RelativeLayout mainContent = findViewById(R.id.activity_sign_in);
        // Title text displays: "Sign In"
        final TextView titleText = findViewById(R.id.signInText);
        Button signInButton = findViewById(R.id.signInButton);
        // Initialize the UI
        initUI(titleText);

        // onclick listener for the submit button
        signInButton.setOnClickListener(v -> {
            // Get the values from the editext fields
            HashMap<String, String> enteredDetails = getEnteredDetails();
            if (enteredDetails == null) {
                return;
            }

            // Open the loading animation
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignIn.this)
                .setCancelable(false);
            // Get the views
            LayoutInflater inflater = this.getLayoutInflater();
            View createView = inflater.inflate(R.layout.sign_in_loader, null);
            dialogBuilder.setView(createView);
            AlertDialog alertDialog = dialogBuilder.create();
            showLoader(alertDialog, true);

            // Thread for authentication...
            new Thread(() -> {
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

            }).start();
        });
    }




    // performAuth returns a hashmap containing the user details and a flag which tell us if the auth was a success, it authenticates using the authenticator class
    private HashMap<String, Object> performAuth(HashMap<String, String> enteredDetails) throws Exception{
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




    // getEnteredDetails returns the details the user has currently entered into the sign in form
    @Nullable
    private HashMap<String, String> getEnteredDetails() {
        final String studentID = ((EditText) findViewById(R.id.studentID)).getText().toString();
        final String password = ((EditText) findViewById(R.id.studentPassword)).getText().toString();

        // Check that the detials aren't empty
        if (studentID.isEmpty() || password.isEmpty()) {
            uiHandler.post(() -> {
                Toast.makeText(SignIn.this, "Student ID or Password is empty", Toast.LENGTH_LONG).show();
            });
            return null;
        }

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("studentID", studentID);
        userMap.put("password", password);

        return userMap;
    }




    // initUI initialises the ui by setting the fonts and anything else u might want to happen
    private void initUI(TextView signInText) {signInText.setTypeface(raleway);}




    // updatePrefDetails updates the shared preferences given a specific user
    private void updatePrefDetails(SharedPreferences.Editor editor, User user) {
        Gson jsonEncoder = new Gson();

        // Serialize the user data and put the string in the editor
        editor.putString("user-data", user.toString());
        editor.putBoolean("logged-in", true);

        HashMap<String, String> reminderColours = new HashMap<>();

        reminderColours.put("hw", "#64dd17");
        reminderColours.put("general", "#008cff");

        String hashMapString = jsonEncoder.toJson(reminderColours);

        editor.putString("tag-colours", hashMapString);
        editor.apply();
    }




    // showLoader displays the the user the loader box
    private void showLoader(AlertDialog dialog, boolean load) {
        if (load) {
            // Load the window views into the layout
            // Make the dialog fill the screen
            Window window = dialog.getWindow();
            assert window != null;
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);

            dialog.show();
        } else {
            dialog.cancel();
        }
    }
}