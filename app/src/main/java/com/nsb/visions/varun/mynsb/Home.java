package com.nsb.visions.varun.mynsb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Events.Events;
import com.nsb.visions.varun.mynsb.FourU.*;
import com.nsb.visions.varun.mynsb.Reminders.Create.CreateReminder;
import com.nsb.visions.varun.mynsb.Reminders.Create.CreateReminderHandler;
import com.nsb.visions.varun.mynsb.Reminders.Reminder;
import com.nsb.visions.varun.mynsb.Reminders.Reminders;
import com.nsb.visions.varun.mynsb.Timetable.Timetables;

import org.json.JSONArray;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

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
                    mTextMessage.setText("Your Timetable");
                    loadTimetables();
                    return true;
                case R.id.navigation_events:
                    mTextMessage.setText("Events This Week");
                    loadEvents();
                    return true;
                case R.id.navigation_reminder:
                    mTextMessage.setText("Reminders For Today");
                    loadReminders();
                    return true;
                case R.id.navigation_4u:
                    mTextMessage.setText("The 4U Paper");
                    load4U();
                    return true;
                case R.id.navigation_calendar:
                    mTextMessage.setText("School Calendar");
                    return true;
            }
            return false;
        }

    };





    // TODO: Refactor so we can remove all those massive blocks of text
    /*
        @ UTIL FUNCTIONS ==============================
     */
    /* load4U loads all the 4U articles when the navigation_4u button is clicked
            @params;
                nil
     */
     private void load4U() {
         flipper.setDisplayedChild(0);
         if (!loaded.get("4U")) {
            // Set up the four u client
            FourU fourU = new FourU(getApplicationContext());
            // Load the ui
             pushUI(fourU, 0);
            // Set the loaded flag to true
            loaded.put("4U", true);
        }
    }
    /* loadReminders attains all the user's reminders for the current day
            @params;
                nil
     */
    private void loadReminders() {
        flipper.setDisplayedChild(1);
        if (!loaded.get("Reminders")) {
            // Set up the reminder client
            Reminders reminder = new Reminders(getApplicationContext(), this.sharePref);
            // Load the ui
            pushUI(reminder, 1);
            // Set the loaded flag to true
            loaded.put("Reminders", true);
        }
    }
    /* loadEvents loads all the events from the api into
           @params;
                nil
     */
    private void loadEvents() {
        flipper.setDisplayedChild(2);
        if (!loaded.get("Events")) {
            // Set up the events client
            Events events = new Events(getApplicationContext());
            // Load the ui
            pushUI(events, 2);
            // Set the loaded flag for events
            loaded.put("Events", true);
        }
    }
    /* loadTimetables loads timetables for the current day into view
            @params;
                nil
     */
    private void loadTimetables() {
        flipper.setDisplayedChild(3);
        if (!loaded.get("Timetables")) {
            // Set up the timetables client
            Timetables timetables = new Timetables(getApplicationContext());
            pushUI(timetables, 3);
            // Set the loaded flag to true
            loaded.put("Timetables", true);
        }
    }


    /* pushUI takes a loader and a child index and pushes all the data into the currently selected view
            @params;
                Loader loader (The object doing most of the heavy lifting)
                int childIndex (The index of the child in our layout fliper)

     */
    private void pushUI(Loader loader, int childIndex) {
        RelativeLayout mainHolder = (RelativeLayout) flipper.getChildAt(childIndex);
        SwipeRefreshLayout swiperLayout = (SwipeRefreshLayout) mainHolder.getChildAt(0);
        RecyclerView contentHolder = (RecyclerView) swiperLayout.findViewById(R.id.recyclerLoader);
        ProgressBar progressBar = (ProgressBar) swiperLayout.findViewById(R.id.loader);
        // Load the UI
        loader.loadUI(contentHolder, swiperLayout, progressBar, errorHolder, uiHandler);

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
            LayoutInflater inflater = this.getLayoutInflater();
            CreateReminderHandler.handleCreateReminderButton(createReminder, inflater, this, getApplication());
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Set the default selected tab
        // TBH: This is a super hacky solution but i guess it works
        View DefaultTab = navigation.findViewById(R.id.navigation_timetable);
        DefaultTab.performClick();
    }
}
