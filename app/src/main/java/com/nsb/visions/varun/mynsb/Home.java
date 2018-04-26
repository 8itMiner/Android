package com.nsb.visions.varun.mynsb;

import android.content.Context;
import android.content.Intent;
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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.nsb.visions.varun.mynsb.Calendar.Calendars;
import com.nsb.visions.varun.mynsb.Common.HandleFABClicks;
import com.nsb.visions.varun.mynsb.Common.Loader;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.Events.Events;
import com.nsb.visions.varun.mynsb.FourU.FourU;
import com.nsb.visions.varun.mynsb.Jobs.TimetableSync;
import com.nsb.visions.varun.mynsb.Reminders.Reminders;
import com.nsb.visions.varun.mynsb.Timetable.Timetables;

import java.sql.Time;
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
                    mTextMessage.setText("Your Timetable");
                    Timetables timetables = new Timetables(getApplicationContext(), sharePref, false);
                    pushUI(timetables, 3, "Timetables");
                    return true;
                case R.id.navigation_events:
                    mTextMessage.setText("Events This Week");
                    // Set up the events client
                    Events events = new Events(getApplicationContext());
                    // Load the ui
                    pushUI(events, 2, "Events");
                    return true;
                case R.id.navigation_reminder:
                    mTextMessage.setText("Reminders For Today");
                    // Set up the reminder client
                    Reminders reminder = new Reminders(getApplicationContext(), sharePref);
                    // Load the ui
                    pushUI(reminder, 1, "Reminders");
                    return true;
                case R.id.navigation_4u:
                    mTextMessage.setText("The 4U Paper");
                    FourU fourU = new FourU(getApplicationContext());
                    pushUI(fourU, 0, "4U");
                    return true;
                case R.id.navigation_calendar:
                    mTextMessage.setText("School Calendar");
                    Calendars calendars = new Calendars(getApplicationContext());
                    pushUI(calendars, 4, "Calendar");
                    return true;
            }
            return false;
        }

    };

    /*
        @ START UTIL FUNCTIONS ===============================
     */
    /* pushUI takes a loader and a child index and pushes all the data into the currently selected view
            @params;
                Loader loader (The object doing most of the heavy lifting)
                int childIndex (The index of the child in our layout fliper)

     */
    private void pushUI(Loader loader, int childIndex, String entryName) {
        flipper.setDisplayedChild(childIndex);
        RelativeLayout mainHolder = (RelativeLayout) flipper.getChildAt(childIndex);
        SwipeRefreshLayout swiperLayout = (SwipeRefreshLayout) mainHolder.getChildAt(1);
        RecyclerView contentHolder = swiperLayout.findViewById(R.id.recyclerLoader);

        // Determine if the current view parsed into this is a reminder view, allow the timetable view as well coz it has a special expand feature
        if (loader.getClass() == Reminders.class || loader.getClass() == Timetables.class) {
            // Add the scroller, check function documentation for more details
            initFAB(loader, mainHolder, contentHolder);
        }

        // Hide any current errors so we dont get weird views
        Loader.showErrors(contentHolder, errorHolder, false);
        // Main if
        if (!loaded.get(entryName)) {
            ProgressBar progressBar = mainHolder.findViewById(R.id.loader);
            // Load the UI
            loader.loadUI(contentHolder, swiperLayout, progressBar, errorHolder, uiHandler);
            loaded.put(entryName, true);
        }
    }



    /* initFAB initialises the scroller so that when we scroll down on the reminders view the fab is hidden so that we can read our reminders, it also inits the FAB to be clicked ;)
            @params;
                View parentView
                RecyclerView contentHolder

     */
    private void initFAB(Loader loader, View parentView, RecyclerView contentHolder) {
        // Get our fab var
        FloatingActionButton FAB = parentView.findViewById(R.id.floaterButton);

        // Setup the FAB to be clicked
        // Attach an onclick listener to the floaterButton used by the reminder view and the timetable view
        FAB.setOnClickListener(v -> {
            LayoutInflater inflater = this.getLayoutInflater();
            HandleFABClicks.switchHandler(loader, FAB, inflater, this, getApplication());
        });

        // Add a scroll listener to the recycler so that it hides our FAB when the page is scrolled
        contentHolder.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // Calculate what to do
                if (dy > 0) {
                    FAB.hide();
                } else if (dy < 0) {
                    FAB.show();
                }

            }
        });
    }
    /*
        @ END UTIL FUNCTIONS ==============================
     */





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.sharePref = getSharedPreferences("MyNSB", Context.MODE_PRIVATE);
        routeUser(this.sharePref, this.sharePref.edit());
        super.onCreate(savedInstanceState);

        // Start the sync timetable activity
        TimetableSync.scheduleTask();

        // Setup the shared preferences
        setContentView(R.layout.activity_home);


        // Push everything into loaded hashmap check line 47 for info regarding what the loaded map does
        loaded.put("4U", false);
        loaded.put("Timetables", false);
        loaded.put("Reminders", false);
        loaded.put("Events", false);
        loaded.put("Calendar", false);

        // Change all the fonts
        AssetManager Am = getApplicationContext().getAssets();

        // Raleway font idk when we use this but we might _/(0_0)\_
        final Typeface Raleway = Typeface.createFromAsset(Am,
                String.format(Locale.US, "fonts/%s", "raleway_regular.ttf"));


        // Start the setupActivity function on a new handler
        uiHandler.post(this::setUpActivity);
    }



    /* setUpActivity sets up the current activity with all the data we need and all the correct click listeners
            @params;
                nil
     */
    private void setUpActivity() {
        // Declare our stuff
        // Set the textview and the recyclerview
        this.mTextMessage = findViewById(R.id.message);
        this.errorHolder = findViewById(R.id.errorText);
        this.flipper = findViewById(R.id.flipper);
        FloatingActionButton FAB = findViewById(R.id.floaterButton);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        View DefaultTab = navigation.findViewById(R.id.navigation_timetable);


        // Only run if there is an active internet connection
        if (Util.isNetworkAvailable(Home.this)) {
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            // Set the default selected tab
            // TBH: This is a super hacky solution but i guess it works
            DefaultTab.performClick();
        } else {
            // Otherwise error
            Toast.makeText(Home.this, "Looks like you aren't connected to the internet. Try again later.", Toast.LENGTH_LONG).show();
            DefaultTab.performClick();
        }
    }



    /* routeUser routes the current user based on the stored shared preferences, if this is the first one a tutorial is shown if they are logged in they are taken to the home screen
        @params;
            SharedPreferences preferences
            SharedPreferences.Editor editor
    */
    private void routeUser(SharedPreferences preferences, SharedPreferences.Editor editor) {
        Intent redirect = new Intent(Home.this, SignIn.class);
        // Determine if this is the first time they have run the app if so then take them to the tutorial
        if (preferences.getBoolean("firstrun", true)) {
            // Set the logged-in flag to false
            editor.putBoolean("logged-in", false);
            // Set the firstrun flag to false
            editor.putBoolean("firstrun", false);
            // Apply changes
            editor.apply();
            // Show the tutorial

            // In the mean time take them to the home page
            startActivity(redirect);
            finish();

            // Determine if the user is logged-in through the flag
        } else if (!(preferences.getBoolean("logged-in", false))) {
            // Redirect the user to the home page
            startActivity(redirect);
            finish();
        }
    }


}
