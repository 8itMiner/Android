package com.nsb.visions.varun.mynsb;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Locale;


public class SignIn extends AppCompatActivity {

    // UI updater handler
    private Handler UIHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Attain shared preferences
        final SharedPreferences SharePref = getSharedPreferences("MyNSB", Context.MODE_PRIVATE);
        final SharedPreferences.Editor Editor = SharePref.edit();

        if (SharePref.getBoolean("firstrun", true)) {
            Editor.putBoolean("logged-in", false);
            Editor.putBoolean("firstrun", false);
            Editor.apply();
            // Show the tutorial
        } else if (SharePref.getBoolean("logged-in", false)) {
            Intent redirect = new Intent(SignIn.this, Home.class);
            startActivity(redirect);
        }


        // Load in the layout after shared pref has been checked
        setContentView(R.layout.activity_sign_in);


        // Loader container used in the rest of the code
        final RelativeLayout loaderView = (RelativeLayout) findViewById(R.id.loaderContainer);
        final RelativeLayout mainContent = (RelativeLayout) findViewById(R.id.activity_sign_in);

        // Change all the fonts
        AssetManager Am = getApplicationContext().getAssets();
        final Typeface Raleway = Typeface.createFromAsset(Am,
                String.format(Locale.US, "fonts/%s", "raleway_regular.ttf"));

        ((TextView) findViewById(R.id.signInText)).setTypeface(Raleway);




        // This is the login button
        Button signInButton = (Button) findViewById(R.id.signInButton);


        // Set an onclick listener for the submit button
        signInButton.setOnClickListener(v -> {


            // Get the values from the editext fields
            final String StudentID = ((EditText) findViewById(R.id.studentID)).getText().toString();
            final String Password = ((EditText) findViewById(R.id.studentPassword)).getText().toString();

            // Determine if they are empty
            if (StudentID.isEmpty() || Password.isEmpty()) {
                Toast.makeText(SignIn.this, "Student ID or Password is empty", Toast.LENGTH_LONG).show();
                return;
            }

            // Split the main tasks into two threads

            // One for UI updates.....
            // Start the loader animation
            UIHandler.post(() -> {
                mainContent.setClickable(false);

                // Set the visibility
                loaderView.setVisibility(View.VISIBLE);
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new AccelerateInterpolator());
                fadeIn.setDuration(200);

                // Set the status bar colour
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
                // Set it and start
                loaderView.startAnimation(fadeIn);
            });




            // Thread for authentication...
            Thread auth = new Thread(() -> {
                // Sleep the thread for that special effect ;)
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Parse the data into the auth function
                Auth authenticator = new Auth(getApplicationContext());
                try {
                    User user = authenticator.auth(StudentID, Password);
                    // Serialize the user data
                    Editor.putString("user-data", user.toString());
                    Editor.putBoolean("logged-in", true);
                    Editor.apply();

                    // Redirect the user
                    Intent moveUser = new Intent(SignIn.this, Home.class);
                    startActivity(moveUser);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    // Set the successful login flag to false
                    Log.d("Error", e.toString());

                    // Refresh the activity...
                    // Close the Relative layout....
                    UIHandler.post(() -> {
                        // Fade out the loader view
                        Animation fadeOut = new AlphaAnimation(1, 0);
                        fadeOut.setDuration(150);
                        fadeOut.setStartOffset(150);
                        fadeOut.setInterpolator(new AccelerateInterpolator());

                        loaderView.startAnimation(fadeOut);
                        loaderView.setVisibility(View.GONE);

                        Toast.makeText(SignIn.this, "Details are invalid", Toast.LENGTH_LONG).show();
                    });
                }
            });
            auth.start();
        });
    }
}