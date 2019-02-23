package com.nsb.visions.varun.mynsb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;


// To NoConnection acitivity is a simple activity that deals with lack of a valid and useful network to use to send requests
public class NoConnection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        final Context context = getApplicationContext();

        // Get the swiperefresh and add a listener to it
        // This listener determine if there is an avaliable network and moves the user to the home page/signin page if there is
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refresher);
        refreshLayout.setOnRefreshListener(() -> {
            // Determine if the network is now available
            if (HTTP.validConnection(context)) {
                Intent moveMe = new Intent(context, SignIn.class);
                context.startActivity(moveMe);
                finish();
            } else {
                Toast.makeText(context, "No network is available :(", Toast.LENGTH_LONG).show();
            }
        });

    }

}
