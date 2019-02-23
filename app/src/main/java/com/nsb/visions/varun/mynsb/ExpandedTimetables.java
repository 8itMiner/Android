package com.nsb.visions.varun.mynsb;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.Timetable.Timetables;


// The ExpandedTimetables is a simple interface that allows students to look up their timetable for any day/week
public class ExpandedTimetables extends AppCompatActivity {


    // Constructor for the activity, AKA, acitivity entry point
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_timetables);

        // Create a new timetable instance
        Timetables timetables = new Timetables(getApplicationContext(), true);
        SwipeRefreshLayout swiper = findViewById(R.id.swiperefresh);

        // Get the parsed day along with us just so that we can easily load the previous thing the dude saw
        timetables.loadUI(findViewById(R.id.recycler), swiper, findViewById(R.id.loader), findViewById(R.id.errorText), new Handler());

        // Setup up spinners that hold the selections for the user
        Spinner day = findViewById(R.id.day);
        Spinner week = findViewById(R.id.week);
        // Determine the ID of the week and day
        int weekID = timetables.getWeek().equals("A") ? 0 : 1;
        int dayID = Util.stringToDayInt(timetables.getDayStr())-1;
        // Load up the spinners
        day.setSelection(dayID);
        week.setSelection(weekID);
        // Initialize our spinner colours
        initSpinner(day);
        initSpinner(week);


        // Click listener on the submit button
        findViewById(R.id.submissionButton).setOnClickListener((v) -> {
            String selectedDay = day.getSelectedItem().toString();
            String selectedWeek = week.getSelectedItem().toString();

            // Convert the day and week into a number that the API can read
            String splitWeek = selectedWeek.split(" ")[1];
            int dayInt = Util.stringToDayInt(selectedDay);
            // Format the day based on the week
            if (splitWeek.equals("B")) {
                dayInt += 5;
            }

            // Set the new day for the timetable class and reload the content
            timetables.setCurrDay(dayInt);
            timetables.setDayAndUpdateBellTimes(selectedDay);
            timetables.loadUI(findViewById(R.id.recycler), swiper, findViewById(R.id.loader), findViewById(R.id.errorText), new Handler());
        });
    }





    // initSpinner sets up the individual spinners for data entry
    private void initSpinner(Spinner spinner) {
        // Set the triangle colour
        spinner.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        // Change the selected text colour
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }
}