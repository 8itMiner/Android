package com.nsb.visions.varun.mynsb.Reminders;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.Common.ReminderColours;
import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Reminder> reminders = new ArrayList<>();
    private SharedPreferences preferences;




    // Constructor
    ReminderAdapter(List<Reminder> reminders, SharedPreferences prefs) {
        this.reminders = reminders;
        this.preferences = prefs;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reycycler_text_header, parent, false);

                return new DayHolder(v);
            case 2:
                 v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reminder_card, parent, false);
                return new ReminderHolder(v);
            default:
                return null;
        }
    }




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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




    // getItemViewType determines the item's view type
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
    public int getItemCount() {return reminders.size();}




    // Reminder holder class, see res/layout/reminder_card.xml
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

        DayHolder(View itemView) {
            super(itemView);
            this.dayTitle = itemView.findViewById(R.id.dayTextTitle);
        }
    }




    // convertToAMPM takes a date and spits out the string version of that date in am pm form
    private String convertToAMPM(Date time) {

        String modifier = "";
        String dateTime = Util.formateDate(time, "yyyy-MM-dd HH:mm");


        // Get the raw time
        String rawTime = dateTime.split(" ")[1];
        // Get the hour as 24 time and minutes
        String hour24 = rawTime.split(":")[0];
        String minutes = rawTime.split(":")[1];
        // Convert the hour
        Integer hour = Integer.parseInt(hour24);

        if (hour != 12 && hour != 0) {
            modifier = hour < 12 ? "am" : "pm";
            hour %= 12;
        } else {
            modifier = (hour == 12 ? "pm" : "am");
            hour = 12;
        }
        // Concat and return
        return hour.toString() + ":" + minutes + " " + modifier;
    }




    // getColour reads the sharedPreferences for a user, gets the tag colours and determines what colour is the correct one
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
}
