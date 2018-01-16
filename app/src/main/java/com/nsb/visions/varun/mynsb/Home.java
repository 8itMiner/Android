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

import java.util.Locale;

public class Home extends AppCompatActivity {

    private TextView mTextMessage;
    private RecyclerView container;
    private TextView errorHolder;
    private ViewFlipper flipper;
    private Handler uiHandler = new Handler();
    static boolean HomeOpen = false;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("Home");
                    if (!HomeOpen) {
                        HomeOpen = true;
                        return true;
                    } else {
                        return true;
                    }
                case R.id.navigation_dashboard:
                    mTextMessage.setText("Bulletin");
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText("Notifications");
                    return true;
                case R.id.more_dashboard:
                    return false;
                case R.id.navigation_4u:
                    flipper.setDisplayedChild(0);
                    mTextMessage.setText("4U");
                    try {
                        FourU.LoadUI(container, errorHolder, getApplicationContext(), uiHandler);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
