package com.nsb.visions.varun.mynsb.Reminders.Create;

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
import android.widget.Toast;

import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.R;
import com.nsb.visions.varun.mynsb.Reminders.Reminder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;

import static java.util.TimeZone.getDefault;

/**
 * Created by varun on 11/02/2018.
 */

public class CreateReminderHandler {


    private static AlertDialog alertDialog;
    private static FloatingActionButton fab;
    private static Button closeButton;
    private static Button createReminder;
    private static EditText pickDate;
    private static EditText pickTime;




    // handleCreateReminderButton handles all reminder creation stuff once the create reminder FAB has been clicked
    public static void handleCreateReminderButton(FloatingActionButton fab, LayoutInflater inflater, Context context, Application application) {

        // Create the dialogue that shows us the form
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View createView = inflater.inflate(R.layout.create_reminder_dialog, null);
        dialogBuilder.setView(createView);
        AlertDialog alertDialog = dialogBuilder.create();

        // Make the dialog fill the screen
        Window window = alertDialog.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);

        alertDialog.show();
        fab.setClickable(false);

        // Set up the sub buttons in the modal
        Button closeButton = createView.findViewById(R.id.cancelButton);
        Button createReminder = createView.findViewById(R.id.createReminderButton);
        EditText pickDate = createView.findViewById(R.id.reminderDate);
        EditText pickTime = createView.findViewById(R.id.reminderTime);

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




    // setupHandlers sets up the button click handlers so that the buttons respond to certain actions
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
                handledDateEntry(calendar, year, monthOfYear, dayOfMonth);
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
                handleTimeEntry(calendar, hourOfDay, minuteOfHour);
            };

            TimePickerDialog picker = new TimePickerDialog(context, time, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
            picker.setTitle("");
            picker.show();
        });

        // Setup a click listener for handling a createReminder click
        createReminder.setOnClickListener((v) -> {
            handleReminderSubmission(context, application);
        });
    }




    // handleReminderCreation creates a reminder and pushes it into the API given specific details and parameters
    private static void handleReminderCreation(String reminderSubject, String reminderBody, String reminderDate, String reminderTime, String tag, Application application) {
        try {
            // Create a time that can be parsed into the reminders class
            String reminderDateTimeRaw = reminderDate + " " + reminderTime;
            Date prePass = Util.parseDate(reminderDateTimeRaw, "dd/MM/yyyy HH:mm");
            String finalDate = Util.formateDate(prePass, "yyyy-MM-dd'T'HH:mm:ss'Z'");

            CreateReminder.create(application.getApplicationContext(),
                new Reminder(reminderSubject, reminderBody, new JSONArray().put(0, tag), finalDate));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    // handleDateEntry handles the logic of date entry and what to do with invalid dates
    private static void handledDateEntry(Calendar calendar, int year, int monthOfYear, int dayOfMonth) {
        // Create a calendar instance
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        pickDate.setText(Util.formateDate(calendar.getTime(), "dd/MM/yyyy"));
    }




    // handleTimeEntry is like handleDateEntry except for times
    private static void handleTimeEntry(Calendar calendar, int hourOfDay, int minuteOfHour) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minuteOfHour);
        pickTime.setText(Util.formateDate(calendar.getTime(), "kk:mm"));
    }




    // handleReminderSubmission handles the submission of a reminder whenever the create reminder button is clicked, It also performs error checks on the fields within the form
    private static void handleReminderSubmission(Context context, Application application) {
        String body =  ((EditText)  alertDialog.findViewById(R.id.reminderBody)).getText().toString();
        String date =  ((EditText)  alertDialog.findViewById(R.id.reminderDate)).getText().toString();
        String time =  ((EditText)  alertDialog.findViewById(R.id.reminderTime)).getText().toString();
        String tag  =  ((EditText)  alertDialog.findViewById(R.id.setTag)).getText().toString();

        if (date.isEmpty() || time.isEmpty() || tag.isEmpty() || body.isEmpty()) {
            Toast.makeText(context, "Some fields have not been filled out", Toast.LENGTH_LONG).show();
            return;
        }

        // Attain the text fields we want
        handleReminderCreation(tag, body, date, time, tag, application);
        alertDialog.cancel();
        fab.setClickable(true);
    }
}
