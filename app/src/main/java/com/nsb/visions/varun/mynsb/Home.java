package com.nsb.visions.varun.mynsb;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class Home extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("Home");
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText("Dashboard");
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText("Notifications");
                    return true;
                case R.id.more_dashboard:
                    /**
                     * TODO: Handle the more tab
                     **/
                    return false;
                case R.id.navigation_4u:
                    /**
                     * Todo: Create 4U layout
                     */
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

        mTextMessage = (TextView) findViewById(R.id.message);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Set the default selected tabl
        // TBH: This is a super hacky solution but i guess it works
        View DefaultTab = (View) navigation.findViewById(R.id.navigation_home);
        DefaultTab.performClick();
    }

}
