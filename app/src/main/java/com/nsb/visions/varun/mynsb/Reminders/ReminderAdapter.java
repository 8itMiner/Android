package com.nsb.visions.varun.mynsb.Reminders;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.Common.ReminderColours;
import com.nsb.visions.varun.mynsb.R;

import java.sql.Date;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderHolder> {

    private List<Reminder> reminders;
    private SharedPreferences preferences;

    // Constructor
    public ReminderAdapter(List<Reminder> reminders, SharedPreferences prefs) {
        this.reminders = reminders;
        this.preferences = prefs;
    }





    /*
        OVERRIDDEN FUNCTIONS =========================
     */
    @Override
    public ReminderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.reminder_card, parent, false);

        return new ReminderHolder(v);
    }

    @Override
    public void onBindViewHolder(ReminderHolder holder, int position) {
        Reminder reminder = reminders.get(position);
        // Determine what colour to show the user
        String colour = getColour(reminder, this.preferences);

        // Start setting the information
        holder.colour.setBackgroundColor(Color.parseColor(colour));
        holder.reminderBody.setText(reminder.body);
        holder.time.setText(convertToAMPM((Date) reminder.time));
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }
    /*
        END OVERRIDDEN FUNCTIONS =========================
     */





    // Reminder holder class
    public class ReminderHolder extends RecyclerView.ViewHolder {
        TextView reminderBody;
        TextView time;
        RelativeLayout colour;

        public ReminderHolder(View itemView) {
            super(itemView);

            reminderBody = (TextView) itemView.findViewById(R.id.reminderBody);
            time = (TextView) itemView.findViewById(R.id.time);
            colour = (RelativeLayout) itemView.findViewById(R.id.colour);
        }
    }





    /*
        Utility functions ====================
     */
    /* convertToAMPM takes a date and spits out the string version of that date in am pm form
        @params;
            Date time
     */
    private String convertToAMPM(Date time) {
        // Get the raw time
        String rawTime = time.toString().split(" ")[1];
        // Get the hour as 24 time and minutes
        String hour24 = rawTime.split(":")[0];
        String minutes = rawTime.split(":")[1];
        // Convert the hour
        Integer hour = Integer.parseInt(hour24);
        // Determine the modifier
        String modifier = hour < 12 ? "am" : "pm";
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
    private String getColour(Reminder reminder, SharedPreferences preferences) {
        // Get the full hashmap of colours
        ReminderColours colours = new ReminderColours(this.preferences);

        String colour = colours.tagColours.get("general");
        // Iterate over reminder tags to determine first suitable candidate
        for (String tag : reminder.tags) {
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
