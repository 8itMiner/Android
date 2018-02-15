package com.nsb.visions.varun.mynsb.Reminders.Create;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nsb.visions.varun.mynsb.R;
import com.nsb.visions.varun.mynsb.Reminders.Reminder;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.TimeZone.getDefault;

/**
 * Created by varun on 11/02/2018. Coz varun is awesome as hell :)
 */

public class CreateReminderHandler {


    private static AlertDialog alertDialog;
    private static FloatingActionButton fab;
    private static Button closeButton;
    private static Button createReminder;
    private static Button pickDate;
    private static Button pickTime;

    /*
    UTIL FUNCTIONS ======================
    */
    /* handleCreate reminders handles all reminder creation stuff once the create reminder FAB has been clicked
            @params;
                FloatingActionButton fab,
                LayoutInflater inflater,
                Context context,
                Application application
     */
    public static void handleCreateReminderButton(FloatingActionButton fab, LayoutInflater inflater, Context context, Application application) {
        // Setup a dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        // Get the views
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

        // Set up the variables that we need
        CreateReminderHandler.fab = fab;
        CreateReminderHandler.alertDialog = alertDialog;
        CreateReminderHandler.closeButton = closeButton;
        CreateReminderHandler.createReminder = createReminder;
        CreateReminderHandler.pickDate = pickDate;
        CreateReminderHandler.pickTime = pickTime;

        // Start setting up the handlers
        setupHandlers(context, application);
    }


    /* setupHandlers sets up the button click handlers so that the buttons respond to certain actions
            @params;
                Context context
                Application application
     */
    private static void setupHandlers(Context context, Application application) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(getDefault());
        // Close button handler
        closeButton.setOnClickListener((v) -> {
            alertDialog.cancel();
            fab.setClickable(true);
        });
        // Set up the date/time picker buttons
        pickDate.setOnClickListener((v) -> {
            // Init a datePicker dialog
            DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                handledDateEntry(alertDialog, calendar, year, monthOfYear, dayOfMonth);
            };

            DatePickerDialog picker = new DatePickerDialog(context, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        });
        // Setup the time picker button
        pickTime.setOnClickListener((v) -> {
            // Initiate a timePicker dialog
            TimePickerDialog.OnTimeSetListener time = (view, hourOfDay, minuteOfHour) -> {
                handleTimeEntry(alertDialog, calendar, hourOfDay, minuteOfHour);
            };

            TimePickerDialog picker = new TimePickerDialog(context, time, calendar.HOUR_OF_DAY, calendar.MINUTE, true);
            picker.setTitle("");
            picker.show();
        });
        // Setup a click listener for handling a createReminder click
        createReminder.setOnClickListener((v) -> {
            handleReminderSubmission(context, application);
        });

    }


    /* handleReminderCreation creates a reminder given specific details and parameters
            @params;
                String reminderSubject
                String reminderBody
                String reminderDate
                String reminderTime
                String tag
                Application application

     */
    private static boolean handleReminderCreation(String reminderSubject, String reminderBody,
                                                  String reminderDate, String reminderTime, String tag, Application application) {
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


            CreateReminder.createReminder(application.getApplicationContext(),
                new Reminder(reminderSubject, reminderBody, new JSONArray().put(0, tag), finalDate));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }



    /* handleDateEntry handles the logic of date entry and what to do with invalid dates
            @params;
               AlertDialog alertDialog
               Calendar calendar
               int year
               int monthOfYear
               int dayOfMonth
     */
    private static void handledDateEntry(AlertDialog alertDialog, Calendar calendar, int year, int monthOfYear, int dayOfMonth) {
        // Create a calendar instance
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        // SDF for formatting the response
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Attain the selected date display
        TextView dateDisplay = (TextView) alertDialog.findViewById(R.id.setDate);
        dateDisplay.setText(dateFormat.format(calendar.getTime()));
    }


    /* handleTimeEntry is like handleDateEntry except for times
            @params;
               AlertDialog alertDialog
               Calendar calendar
               int hourOfDay
               int minuteOfHour
     */
    private static void handleTimeEntry(AlertDialog alertDialog, Calendar calendar, int hourOfDay, int minuteOfHour) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minuteOfHour);

        // SDF for formatting the response
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm");

        // Set the selected time
        TextView timeDisplay = (TextView) alertDialog.findViewById(R.id.setTime);
        timeDisplay.setText(dateFormat.format(calendar.getTime()));
    }


    /* handleReminderSubmission handles the submission of a reminder whenever the create reminder button is clicked
            @params;
                Context context
                Application application
     */
    private static void handleReminderSubmission(Context context, Application application) {
        @SuppressLint("CutPasteId")
        String subject = ((EditText) alertDialog.findViewById(R.id.reminderSubject)).getText().toString();
        String body = ((EditText) alertDialog.findViewById(R.id.reminderBody)).getText().toString();
        @SuppressLint("CutPasteId")
        String date = (String) ((TextView) alertDialog.findViewById(R.id.setDate)).getText();
        String time = (String) ((TextView) alertDialog.findViewById(R.id.setTime)).getText();
        String tag = ((EditText) alertDialog.findViewById(R.id.setTag)).getText().toString();

        if (date.equals("01/09/2002") || time.equals("27:02") || subject.isEmpty() || body.isEmpty()) {
            Toast.makeText(context, "Some fields have not been filled out", Toast.LENGTH_LONG).show();
            return;
        }

        // Attain the text fields we want
        boolean success = handleReminderCreation(subject, body, date, time, tag, application);
        if (!success) {
            // Throw a toast
            Toast.makeText(context, "Could not create reminder", Toast.LENGTH_LONG).show();
            alertDialog.cancel();
            fab.setClickable(true);
            return;
        }

        // Close the dialog
        alertDialog.cancel();
        fab.setClickable(true);
        Toast.makeText(context, "Please refresh the page for your reminders to show up", Toast.LENGTH_LONG).show();
    }


}
