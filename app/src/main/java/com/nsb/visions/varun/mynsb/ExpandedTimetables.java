package com.nsb.visions.varun.mynsb;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.R;
import com.nsb.visions.varun.mynsb.Timetable.Timetables;

// Expanded timetables are an overall view of your future timetables, the feature is rather sinmple and therefore should be prioritised for completion only after the other components are complete


public class ExpandedTimetables extends AppCompatActivity {


    private SharedPreferences sharePref;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_timetables);

        // Load the share prefs
        this.sharePref = getSharedPreferences("MyNSB", Context.MODE_PRIVATE);

        // Do this once a spinner option has been pressed
        // Attain the corresponding variables

        // Create a new timetable instance
        Timetables timetables = new Timetables(getApplicationContext(), sharePref, true);
        SwipeRefreshLayout swiper = findViewById(R.id.swiperefresh);


        // Get the parsed day along with us just so that we can easily load the previous thing the dude saw
        timetables.loadUI(findViewById(R.id.recycler), swiper, findViewById(R.id.loader), findViewById(R.id.errorText), new Handler());



        // Setup our spinners
        Spinner day = findViewById(R.id.day);
        Spinner week = findViewById(R.id.week);


        // Initialize our spinner colours
        initSpinner(day);
        initSpinner(week);


        // Determine if the submit button has been clicked
        findViewById(R.id.submissionButton).setOnClickListener((v) -> {
            // Get the content from the spinners
            String dayStr = day.getSelectedItem().toString();
            String weekStr = week.getSelectedItem().toString();

            // Convert the day into a number that the api can read
            // Trim the week
            String splitWeek = weekStr.split(" ")[1];

            // Convert the day into an integer
            int dayInt = Util.stringToDayInt(dayStr);
            // Format the day based on the week
            if (splitWeek.equals("B")) {
                dayInt += 5;
            }


                // Set the new url for the timetable class and reload the content
            timetables.setURL("http://35.189.45.152:8080/api/v1/timetable/Get?Day=" + String.valueOf(dayInt));
            timetables.loadUI(findViewById(R.id.recycler), swiper, findViewById(R.id.loader), findViewById(R.id.errorText), new Handler());



        });
        // Get the recycler view and load the selected date into it
    }




    // Function that allows us to set the selected colour for our spinner
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
