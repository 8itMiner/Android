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
 * Created by varun on 11/02/2018. Coz varun is awesome as hell :)
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarView> {

    public List<Calendar> calendars = new ArrayList<>();

    public CalendarAdapter(List<Calendar> calendars) {
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

        public TextView entryName;
        public TextView entryTime;

        public CalendarView(View itemView) {
            super(itemView);

            this.entryName = (TextView) itemView.findViewById(R.id.calendarEntryName);
            this.entryTime = (TextView) itemView.findViewById(R.id.date);
        }
    }

}
