package com.nsb.visions.varun.mynsb.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nsb.visions.varun.mynsb.Common.ReminderColours;
import com.nsb.visions.varun.mynsb.Exception.MyNSBException;
import com.nsb.visions.varun.mynsb.Exception.MyNSBExceptionType;
import com.nsb.visions.varun.mynsb.R;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences sharedPrefs;

    private void createToast(MyNSBException exception) {
        Toast.makeText(SettingsActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
    }

    private void createReminder(String name, String colour) throws MyNSBException {
        if (name.isEmpty() || colour.isEmpty()) {
            throw new MyNSBException(MyNSBExceptionType.SETTINGS_MISSING_FIELDS);
        }

        ReminderColours rc = new ReminderColours(this.sharedPrefs);
        rc.createTag(name, colour);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.sharedPrefs = getSharedPreferences("MyNSB", Context.MODE_PRIVATE);

        // Various views for reminder creation
        Button reminderButton = findViewById(R.id.create_reminder_tag);
        EditText reminderName = findViewById(R.id.reminder_name_edit);
        EditText reminderColour = findViewById(R.id.reminder_colour_edit);

        reminderButton.setOnClickListener(view -> {
            try {
                createReminder(reminderName.getText().toString(), reminderColour.getText().toString());
            } catch (MyNSBException exception) {
                createToast(exception);
            }

            reminderName.setText("");
            reminderColour.setText("");
        });
    }
}
