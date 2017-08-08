package com.nsb.visions.varun.mynsb;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;



public class SignIn extends AppCompatActivity {

    // UI updater handler
    private Handler UIHandler      = new Handler();
    private Handler UIHandlerClose = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Attain shared preferences
        final SharedPreferences SharePref = getSharedPreferences("MyNSB", Context.MODE_PRIVATE);
        final SharedPreferences.Editor Editor = SharePref.edit();

        // Check to see if they have logged in before before the layout loads
        if (SharePref.contains("User_Details")) {
            Intent Redirect = new Intent(getApplicationContext(), Home.class);
            startActivity(Redirect);
        // Check to see if this is the first time the app has been opened
        } else if (SharePref.getBoolean("firstrun", true)) {
            Editor.putBoolean("firstrun", false);
            Editor.commit();
            Toast.makeText(SignIn.this, "First Run", Toast.LENGTH_LONG).show();
        }

        // Load in the layout after shared pref has been checked
        setContentView(R.layout.activity_sign_in);

        // Change all the fonts
        AssetManager Am = getApplicationContext().getAssets();

        final Typeface Raleway = Typeface.createFromAsset(Am,
                String.format(Locale.US, "fonts/%s", "raleway_regular.ttf"));

        ((TextView) findViewById(R.id.signInText)).setTypeface(Raleway);

        // This is the login button
        final Button SignInButton = (Button) findViewById(R.id.signInButton);
        // Declare the loadreveal
        final View LoadReveal = (View) findViewById(R.id.circle);


        /**
            TODO: Remove this section once the API is complete
         **/
        ((ImageView) findViewById(R.id.mynsbLogo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Req = new Intent(getApplicationContext(), Home.class);
                startActivity(Req);
            }
        });



        /**   Create an event listener for the sign in button
              Listener => OnClickListener
        **/
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the editext fields
                final String StudentID = ((EditText) findViewById(R.id.studentID)).getText().toString();
                String Password  = ((EditText) findViewById(R.id.studentPassword)).getText().toString();

                // Split the main tasks into two threads
                // One for http requests
                final Thread SignIn = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Send Request
                        int Status = 0;


                        // Close the cool circle thing if there is an error
                        if (Status != 200) {
                            UIHandlerClose.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Alert the person
                                    Toast.makeText(SignIn.this, "Invalid Login Details", Toast.LENGTH_LONG).show();
                                    // Get the center
                                    int cx = (LoadReveal.getWidth() / 2);
                                    int cy = (LoadReveal.getHeight() / 2);

                                    // Get the radius for the clipping circle_button
                                    float FinalRadius = (float) Math.hypot(cx, cy);

                                    // Declare animator
                                    Animator anim =
                                            ViewAnimationUtils.createCircularReveal(LoadReveal, cx, cy, FinalRadius, 0);
                                    // Add listener
                                    anim.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            // Make the view visible
                                            LoadReveal.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                    anim.start();
                                }
                            });
                        } else {
                            // Push all the values into out sharedprefrences
                            String Push = String.format(Locale.US, "studentdID:%s token:%s fname:%s lname:%s", StudentID, "", "");
                            Editor.putString("User_Details", Push);
                            Editor.commit();
                            // Redirect to the hompage
                            Intent Redirect = new Intent(getApplicationContext(), Home.class);
                            startActivity(Redirect);
                        }
                    }
                });
                SignIn.start();

                // One for UI updates and super cool animations
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Get the center of the circle_button
                        int cx = (LoadReveal.getWidth() / 2);
                        int cy = (LoadReveal.getHeight() / 2);

                        // Get the radius for the clipping circle_button
                        float FinalRadius = (float) Math.hypot(cx, cy);

                        // Create animator
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(LoadReveal, cx, cy, 0, FinalRadius);
                        // Make the view visible
                        LoadReveal.setVisibility(View.VISIBLE);
                        // Start the animation
                        anim.start();

                        // Change the font of the loading text
                        ((TextView) findViewById(R.id.loaderText)).setTypeface(Raleway);
                    }
                });
            }
        });
    }
}