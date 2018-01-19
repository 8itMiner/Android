package com.nsb.visions.varun.mynsb;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nsb.visions.varun.mynsb.FourU.*;
import com.nsb.visions.varun.mynsb.Reminders.Reminder;
import com.nsb.visions.varun.mynsb.Reminders.Reminders;

import java.util.HashMap;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private TextView mTextMessage;
    private RecyclerView container;
    private TextView errorHolder;
    private ViewFlipper flipper;
    private Handler uiHandler = new Handler();
    // Tells us which views have been loaded so we don't load them again
    private HashMap<String, Boolean> loaded = new HashMap<>();



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("Home");
                case R.id.navigation_dashboard:
                    mTextMessage.setText("Bulletin");
                    return true;
                case R.id.navigation_reminder:
                    mTextMessage.setText("Reminders");
                    try {
                        loadReminders();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.more_dashboard:
                    return false;
                case R.id.navigation_4u:
                    try {
                        load4U();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
     private void load4U() throws Exception {
        if (!loaded.get("4U")) {
            flipper.setDisplayedChild(0);
            mTextMessage.setText("4U");
            // Set up the four u client
            FourU fourU = new FourU(getApplicationContext());
            fourU.loadUI(container, errorHolder, uiHandler);
            // Set the loaded flag to true
            loaded.put("4U", true);
        }
    }
    /* loadReminders attains all the user's reminders for the currrent day
            @params;
                nil
     */
    private void loadReminders() throws Exception {
        if (!loaded.get("Reminders")) {
            flipper.setDisplayedChild(1);
            mTextMessage.setText("Reminders");
            // Set up the reminder client
            Reminders reminder = new Reminders(getApplicationContext());
            reminder.loadUI(container, errorHolder, uiHandler);
            // Set the loaded flag
            loaded.put("Reminders", true);
        }
    }
    /*
        @ END UTIL FUNCTIONS ==============================
     */





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Push everything into loaded hashmap
        loaded.put("4U", false);
        loaded.put("Home", false);
        loaded.put("Notifications", false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Change all the fonts
        AssetManager Am = getApplicationContext().getAssets();

        final Typeface Raleway = Typeface.createFromAsset(Am,
                String.format(Locale.US, "fonts/%s", "raleway_regular.ttf"));

        // Set the textview and the recyclerview
        mTextMessage = (TextView) findViewById(R.id.message);
        container = (RecyclerView) findViewById(R.id.recycler);
        errorHolder = (TextView) findViewById(R.id.errorText);
        flipper = (ViewFlipper) findViewById(R.id.flipper);

        // Set the linearlayoutmanager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        container.setLayoutManager(llm);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Set the default selected tabl
        // TBH: This is a super hacky solution but i guess it works
        View DefaultTab = (View) navigation.findViewById(R.id.navigation_home);
        DefaultTab.performClick();
    }

}
