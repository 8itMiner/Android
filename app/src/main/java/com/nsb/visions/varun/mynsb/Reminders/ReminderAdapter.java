package com.nsb.visions.varun.mynsb.Reminders;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.Common.ReminderColours;
import com.nsb.visions.varun.mynsb.R;
import com.nsb.visions.varun.mynsb.Timetable.Subject;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Reminder> reminders = new ArrayList<>();
    private SharedPreferences preferences;

    // Constructor
    ReminderAdapter(List<Reminder> reminders, SharedPreferences prefs) {
        // We need to break apart the reminder list into date categories
        for (int i = 1; i < reminders.size(); i++) {
            // The way we determine if a reminder is on a different day is that we look at the dates, if they are the same there are the same day else
            // different days, this only works because the reminders are in ascending order in terms of the reminder_date_time, make sense?
            // great!
            // Attain the current reminder
            Reminder curr = reminders.get(i);
            Reminder past = reminders.get(i-1);

            // Determine if this is a new day
            if (!(past.date.equals(curr.date))) {
                // Insert a reminder and this just contains
                // Convert date into an actual day
                String day = curr.date;
                reminders.add(i, new Reminder(day, "", null, null));
            }
        }
        // End of appending times to the list


        this.reminders = reminders;
        this.preferences = prefs;
    }





    /*
        OVERRIDDEN FUNCTIONS =========================
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reycycler_text_header, parent, false);

                return new DayHolder(v);
            case 2:
                 v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reminder_card, parent, false);
                return new ReminderHolder(v);

        }
        // Btw this will never happen
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Determine the view type of the current item and perform actions accordingly
        switch (getItemViewType(position)) {
            case 1:
                // TextHolder label
                DayHolder dayHolder = (DayHolder) holder;
                dayHolder.dayTitle.setText(reminders.get(position).subject);
                break;
            case 2:
                // Reminder Holder label
                ReminderHolder reminderHolder = (ReminderHolder) holder;

                Reminder reminder = reminders.get(position);
                // Determine what colour to show the user
                String colour = getColour(reminder);

                // Start setting the information
                reminderHolder.colour.setBackgroundColor(Color.parseColor(colour));
                reminderHolder.reminderBody.setText(reminder.body);
                reminderHolder.time.setText(convertToAMPM(reminder.time));
                reminderHolder.subject.setText(reminder.subject);
                break;
        }
    }


    // Determine the item's view type
    @Override
    public int getItemViewType(int position) {
        // Get the current item with our position
        Reminder currReminder = this.reminders.get(position);
        if (currReminder.time == null) {
            // Item type is 1 or a text label view
            return 1;
        }
        // Default item type
        return 2;
    }


    @Override
    public int getItemCount() {
        return reminders.size();
    }
    /*
        END OVERRIDDEN FUNCTIONS =========================
     */





    /*
        HOLDER CLASSES =========================
     */
    // Reminder holder class
    class ReminderHolder extends RecyclerView.ViewHolder {
        TextView reminderBody;
        TextView time;
        RelativeLayout colour;
        TextView subject;

        ReminderHolder(View itemView) {
            super(itemView);

            reminderBody = itemView.findViewById(R.id.reminderBody);
            time = itemView.findViewById(R.id.time);
            colour = itemView.findViewById(R.id.colour);
            subject = itemView.findViewById(R.id.subject);
        }
    }


    // Holds the text for a reminder for a specific day
    class DayHolder extends RecyclerView.ViewHolder {

        private TextView dayTitle;

        public DayHolder(View itemView) {
            super(itemView);
            this.dayTitle = itemView.findViewById(R.id.dayTextTitle);
        }
    }
    /*
        END HOLDER CLASSES =========================
     */





    /*
        Utility functions ====================
     */
    /* convertToAMPM takes a date and spits out the string version of that date in am pm form
        @params;
            Date time
     */
    private String convertToAMPM(Date time) {
        // Convert the date into the format we need
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        // Format the date
        String dateTime = simpleDateFormat.format(time);

        // Get the raw time
        String rawTime = dateTime.split(" ")[1];
        // Get the hour as 24 time and minutes
        String hour24 = rawTime.split(":")[0];
        String minutes = rawTime.split(":")[1];
        // Convert the hour
        Integer hour = Integer.parseInt(hour24);
        // Determine the modifier
        String modifier = hour > 12 ? "am" : "pm";
        // Round the modifier
        hour %= 12;

        // Concat and return
        return hour.toString() + ":" + minutes + " " + modifier;
    }


    /* getColour reads the sharedPreferences for a user, gets the tag colours and determines what colour is the correct one
        for the reminder
        @params;
            Reminder reminder
            SharedPreferences preferences

     */
    private String getColour(Reminder reminder) {
        // Get the full hashmap of colours
        ReminderColours colours = new ReminderColours(this.preferences);

        String colour = colours.tagColours.get("general");
        // Iterate over reminder tags to determine first suitable candidate
        for (int i = 0; i < reminder.tags.size(); i++) {
            String tag = reminder.tags.get(i);
            // The hashmap contains that colour
            if (colours.tagColours.containsKey(tag)) {
                colour = colours.tagColours.get(tag);
                break;
            }
        }

        return colour;
    }
    /*
        End Utility functions ====================
     */
}
