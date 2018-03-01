package com.nsb.visions.varun.mynsb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.R;
import com.nsb.visions.varun.mynsb.Timetable.Timetables;

public class ExpandedTimetables extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_timetables);




        // TODO: There are two options to do this, we can have the user prompt for the data in
        // TODO: advance or we can wait for them to make a choice once the activity is done
        // Build the shared preferences
        SharedPreferences preferences = getSharedPreferences("MyNSB", Context.MODE_PRIVATE);

        // Do this once a spinner option has been pressed
        // Attain the corresponding variables

        // Create a new timetable instance
        Timetables timetables = new Timetables(getApplicationContext(), preferences, true);



        // Determine if the submit button has been clicked
        findViewById(R.id.submissionButton).setOnClickListener((v) -> {
            // Get the content from the spinners
            Spinner day = findViewById(R.id.day);
            Spinner week = findViewById(R.id.week);

            String dayStr = day.getSelectedItem().toString();
            String weekStr = week.getSelectedItem().toString();

            // Convert the day into a number that the api can read
            // Trim the week
            String splitWeek = weekStr.split(" ")[0];

            // Convert the day into an integer
            int dayInt = Util.stringToDayInt(dayStr);
            // Format the day based on the week
            if (weekStr.equals("B")) {
                dayInt += 5;
            }

            // Set the new url for the timetable class and reload the content
            timetables.setURL("http://35.189.45.152:8080/api/v1/timetable/Get?Day=" + String.valueOf(dayInt));
            timetables.loadUI(findViewById(R.id.recycler), null, findViewById(R.id.loader), findViewById(R.id.errorText), new Handler());
        });
        // Get the recycler view and load the selected date into it


    }
}
