package com.nsb.visions.varun.mynsb;

// TODO: Add tags to the reminders create capability

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.util.Log;
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

import com.nsb.visions.varun.mynsb.Events.Events;
import com.nsb.visions.varun.mynsb.FourU.*;
import com.nsb.visions.varun.mynsb.Reminders.CreateReminder;
import com.nsb.visions.varun.mynsb.Reminders.Reminder;
import com.nsb.visions.varun.mynsb.Reminders.Reminders;
import com.nsb.visions.varun.mynsb.Timetable.Timetables;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Response;

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
            // Get the content holder recyclerview
             RelativeLayout holderOne = (RelativeLayout) flipper.getChildAt(0);
             SwipeRefreshLayout holderTwo = (SwipeRefreshLayout) holderOne.getChildAt(0);
             RecyclerView contentHolder = (RecyclerView) holderTwo.findViewById(R.id.recyclerLoader);
             ProgressBar progressBar = (ProgressBar) holderOne.findViewById(R.id.loader);
             // Load the UI
            fourU.loadUI(contentHolder, holderTwo, progressBar, errorHolder, uiHandler);
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
            // Get the content holder recyclerview
            RelativeLayout holderOne = (RelativeLayout) flipper.getChildAt(1);
            SwipeRefreshLayout holderTwo = (SwipeRefreshLayout) holderOne.getChildAt(0);
            RecyclerView contentHolder = (RecyclerView) holderTwo.findViewById(R.id.recyclerLoader);
            ProgressBar progressBar = (ProgressBar) holderOne.findViewById(R.id.loader);
            // Load the UI
            reminder.loadUI(contentHolder, holderTwo, progressBar, errorHolder, uiHandler);
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
            // Get the content holder recyclerview
            RelativeLayout holderOne = (RelativeLayout) flipper.getChildAt(2);
            SwipeRefreshLayout holderTwo = (SwipeRefreshLayout) holderOne.getChildAt(0);
            RecyclerView contentHolder = (RecyclerView) holderTwo.findViewById(R.id.recyclerLoader);
            ProgressBar progressBar = (ProgressBar) holderOne.findViewById(R.id.loader);
            // Load the UI
            events.loadUI(contentHolder, holderTwo, progressBar, errorHolder, uiHandler);
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
            // Get all the respective content holders
            RelativeLayout holderOne = (RelativeLayout) flipper.getChildAt(3);
            SwipeRefreshLayout holderTwo = (SwipeRefreshLayout) holderOne.getChildAt(0);
            RecyclerView contentHolder = (RecyclerView) holderTwo.findViewById(R.id.recyclerLoader);
            ProgressBar progressBar = (ProgressBar) holderOne.findViewById(R.id.loader);
            // Disable the swiper from being pulled
            holderTwo.setEnabled(true);
            // Load the UI
            timetables.loadUI(contentHolder, holderTwo, progressBar, errorHolder, uiHandler);
            // Set the loaded flag to true
            loaded.put("Timetables", true);
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
            handleCreateReminderButton(createReminder);
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
    /* handleCreate reminders handles all reminder creation stuff once the create reminder FAB has been clicked
            @params;
                FloatingActionButton fab
                boolean openClose
                    @summary;
                        this boolean refers to weather the create reminder modal should be opened or close
                            True: open
                            False: close
     */
    private void handleCreateReminderButton(FloatingActionButton fab) {
        // Attain the views we require
        CardView createReminderHolder = (CardView) findViewById(R.id.createReminderCard);

        // Setup a dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Home.this);
        // Get the views
        LayoutInflater inflater = this.getLayoutInflater();
        View createView = inflater.inflate(R.layout.create_reminder_dialog, null);
        dialogBuilder.setView(createView);
        AlertDialog alertDialog = dialogBuilder.create();
        // Make the dialog fill the screen
        Window window = alertDialog.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);

        // Show the dialog if they want us to open it
        alertDialog.show();

        // Stop the fab from being clicked in the future
        fab.setClickable(false);

        // Set up the sub buttons in the modal
        Button closeButton = (Button) createView.findViewById(R.id.cancelButton);
        Button createReminder = (Button) createView.findViewById(R.id.createReminderButton);
        Button pickDate = (Button) createView.findViewById(R.id.setDateButton);
        Button pickTime = (Button) createView.findViewById(R.id.setTimeButton);

        // Start setting up the handlers
        setupHandlers(closeButton, createReminder, pickDate, pickTime, fab, alertDialog);
    }

    /* setupHandlers sets up the button click handlers so that the buttons respond to certain actions
            @params;
                Button closeButton
                Button createReminder
                Button pickDate
                Button pickTime
     */
    private void setupHandlers(Button closeButton, Button createReminder, Button pickDate, Button pickTime, FloatingActionButton fab, AlertDialog alertDialog) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());


        // Close button handler
        closeButton.setOnClickListener((v) -> {
            alertDialog.cancel();
            fab.setClickable(true);
        });

        // Set up the date/time picker buttons
        pickDate.setOnClickListener((v) -> {
            // Init a datePicker dialog
            DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // SDF for formatting the response
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                // Attain the selected date display
                TextView dateDisplay = (TextView) alertDialog.findViewById(R.id.setDate);
                dateDisplay.setText(dateFormat.format(calendar.getTime()));

            };

            DatePickerDialog picker = new DatePickerDialog(Home.this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        });

        // Setup the time picker button
        pickTime.setOnClickListener((v) -> {
            // Initiate a timePicker dialog
            TimePickerDialog.OnTimeSetListener time = (view, hourOfDay, minuteOfHour) -> {
                // Auto generated stub
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minuteOfHour);

                // SDF for formatting the response
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm");

                // Set the selected time
                TextView timeDisplay = (TextView) alertDialog.findViewById(R.id.setTime);
                timeDisplay.setText(dateFormat.format(calendar.getTime()));
            };

            TimePickerDialog picker = new TimePickerDialog(Home.this, time, calendar.HOUR_OF_DAY, calendar.MINUTE, true);
            picker.setTitle("");
            picker.show();
        });

        // Setup a click listener for handling a createReminder click
        createReminder.setOnClickListener((v) -> {
            @SuppressLint("CutPasteId")
            String subject = ((EditText) alertDialog.findViewById(R.id.reminderSubject)).getText().toString();
            String body = ((EditText) alertDialog.findViewById(R.id.reminderBody)).getText().toString();
            @SuppressLint("CutPasteId")
            String date = (String) ((TextView) alertDialog.findViewById(R.id.setDate)).getText();
            String time = (String) ((TextView) alertDialog.findViewById(R.id.setTime)).getText();

            if (date.equals("01/09/2002") || time.equals("15:02") || subject.isEmpty() || body.isEmpty()) {
                Toast.makeText(Home.this, "Some fields have not been filled out", Toast.LENGTH_LONG).show();
                return;
            }

            // Star

            // Attain the text fields we want
            boolean success = handleReminderCreation(subject, body, date, time);
            if (!success) {
                // Throw a toast
                Toast.makeText(Home.this, "Could not create reminder", Toast.LENGTH_LONG).show();
                alertDialog.cancel();
                fab.setClickable(true);
                return;
            }

            // Close the dialog
            alertDialog.cancel();
            fab.setClickable(true);
            Toast.makeText(Home.this, "Please refresh the page for your reminders to show up", Toast.LENGTH_LONG).show();

        });

    }

    /* handleReminderCreation creates a reminder given specific details and parameters
            @params;
                String reminderSubject
                String reminderBody
                String reminderDate
                String reminderTime

     */
    private boolean handleReminderCreation(String reminderSubject, String reminderBody, String reminderDate, String reminderTime) {
        // The format the dates will be in post reminder push
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat endParse = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        // The format the dates will be in pre reminder push
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat startParse = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        // Try catch to determine if there was an error during reminder creation
        try {
            // Create a time that can be parsed into the reminders class
            String reminderDateTimeRaw = reminderDate + " " + reminderTime;
            Date prePass = startParse.parse(reminderDateTimeRaw);
            // Post parse string
            String finalDate = endParse.format(prePass);


            CreateReminder.createReminder(getApplicationContext(),
                new Reminder(reminderSubject, reminderBody, new JSONArray().put(0, "hw"), finalDate));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    /*
        END UTIL FUNCTIONS ======================
     */
}
