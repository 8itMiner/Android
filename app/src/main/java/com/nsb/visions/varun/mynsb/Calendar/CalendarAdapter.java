package com.nsb.visions.varun.mynsb.Calendar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.R;

import java.util.ArrayList;
import java.util.List;

/**
 */


// ADAPTERS NEED 0 DOCUMENTATION, YOU SHOULD KNOW ALL THIS ALREADY

// Calendar adapter is an adapter class used to generate the appropriate recyclerViews
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarView> {

    // Calendar list of all calendar instances
    private List<Calendar> calendars = new ArrayList<>();


    /* constructor is just a constructor lmao
           @params;
               Context context

    */
    CalendarAdapter(List<Calendar> calendars) {
        this.calendars = calendars;
    }



    // Create a calendarview from the given viewgroup parent and viewtype
    @Override
    public CalendarView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.calendar_card, parent, false);

        // Return the created viewholder
        return new CalendarView(v);
    }



    @Override
    public void onBindViewHolder(CalendarView holder, int position) {
        Calendar calendar = calendars.get(position);
        // Set up the fields we want
        holder.entryTime.setText(calendar.time);
        holder.entryName.setText(calendar.name);
    }




    @Override
    public int getItemCount() {
        return calendars.size();
    }


    // View class
    class CalendarView extends RecyclerView.ViewHolder {

        TextView entryName;
        TextView entryTime;

        CalendarView(View itemView) {
            super(itemView);

            this.entryName = itemView.findViewById(R.id.calendarEntryName);
            this.entryTime = itemView.findViewById(R.id.date);
        }
    }

}
