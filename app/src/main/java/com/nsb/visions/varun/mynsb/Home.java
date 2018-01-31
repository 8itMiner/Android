package com.nsb.visions.varun.mynsb;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nsb.visions.varun.mynsb.Events.Events;
import com.nsb.visions.varun.mynsb.FourU.*;
import com.nsb.visions.varun.mynsb.Reminders.Reminders;

import java.util.HashMap;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView errorHolder;
    private ViewFlipper flipper;
    private Handler uiHandler = new Handler();
    private SharedPreferences sharePref;
    // Tells us which views have been loaded so we don't load them again and waste precious data
    private HashMap<String, Boolean> loaded = new HashMap<>();



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_timetable:
                    mTextMessage.setText("Home");
                case R.id.navigation_events:
                    loadEvents();
                    return true;
                case R.id.navigation_reminder:
                    loadReminders();
                    return true;
                case R.id.navigation_4u:
                    load4U();
                    return true;
                case R.id.navigation_calendar:
                    return true;
            }
            return false;
        }

    };





    /*
        @ UTIL FUNCTIONS ==============================
     */
    /* load4U loads all the 4U articles when the navigation_4u button is clicked
            @params;
                nil
     */
     private void load4U() {
        if (!loaded.get("4U")) {
            flipper.setDisplayedChild(0);
            mTextMessage.setText("4U");
            // Set up the four u client
            FourU fourU = new FourU(getApplicationContext());
            // Get the content holder recyclerview
            RecyclerView contentHolder = (RecyclerView) ((RelativeLayout) flipper.getChildAt(0)).getChildAt(0);
            // Load the UI
            fourU.loadUI(contentHolder, errorHolder, uiHandler);
            // Set the loaded flag to true
            loaded.put("4U", true);
        }
    }
    /* loadReminders attains all the user's reminders for the current day
            @params;
                nil
     */
    private void loadReminders() {
        if (!loaded.get("Reminders")) {
            flipper.setDisplayedChild(1);
            mTextMessage.setText("Reminders");
            // Set up the reminder client
            Reminders reminder = new Reminders(getApplicationContext(), this.sharePref);
            // Get the content holder recyclerview
            RecyclerView contentHolder = (RecyclerView) ((RelativeLayout) flipper.getChildAt(1)).getChildAt(0);
            // Load the UI
            reminder.loadUI(contentHolder, errorHolder, uiHandler);
            // Set the loaded flag to true
            loaded.put("Reminders", true);
        }
    }
    /* loadEvents loads all the events from the api into
           @params;
                nil
     */
    private void loadEvents() {
        if (!loaded.get("Events")) {
            flipper.setDisplayedChild(2);
            mTextMessage.setText("Events");
            // Set up the events client
            Events events = new Events(getApplicationContext());
            // Get the content holder recyclerview
            RecyclerView contentHolder = (RecyclerView) ((RelativeLayout) flipper.getChildAt(0)).getChildAt(0);
            // Load the UI
            events.loadUI(contentHolder, errorHolder, uiHandler);
            // Set the loaded flag for events
            loaded.put("Events", true);
        }
    }
    /*
        @ END UTIL FUNCTIONS ==============================
     */





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Push everything into loaded hashmap check line 34 for regarding what the loaded map does
        loaded.put("4U", false);
        loaded.put("Timetables", false);
        loaded.put("Reminders", false);
        loaded.put("Events", false);
        loaded.put("Calendar", false);

        super.onCreate(savedInstanceState);
        // Setup the shared preferences
        this.sharePref = getSharedPreferences("MyNSB", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_home);

        // Change all the fonts
        AssetManager Am = getApplicationContext().getAssets();

        final Typeface Raleway = Typeface.createFromAsset(Am,
                String.format(Locale.US, "fonts/%s", "raleway_regular.ttf"));

        // Set the textview and the recyclerview
        mTextMessage = (TextView) findViewById(R.id.message);
        errorHolder = (TextView) findViewById(R.id.errorText);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        FloatingActionButton createReminder = (FloatingActionButton) findViewById(R.id.createReminder);

        // Attach an onclick listener to the create reminder button
        createReminder.setOnClickListener(v -> {
            handleCreateReminderButton();
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Set the default selected tab
        // TBH: This is a super hacky solution but i guess it works
        View DefaultTab = navigation.findViewById(R.id.navigation_timetable);
        DefaultTab.performClick();
    }





    /*
        UTIL FUNCTIONS ======================
     */
        private static void handleCreateReminderButton() {

        }
    /*
        END UTIL FUNCTIONS ======================
     */
}
