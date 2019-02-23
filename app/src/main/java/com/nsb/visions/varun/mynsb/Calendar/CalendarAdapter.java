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



// Calendar adapter is an adapter class used to generate the appropriate recyclerViews
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarView> {
    private List<Calendar> calendar = new ArrayList<>();




    // Constructor
    CalendarAdapter(List<Calendar> calendars) {this.calendar = calendars;}




    // onCreateViewHolder is just an overridden method from the base RecyclerAdapter, it handles the creation of new CalendarRecyclerViews
    @Override
    public CalendarView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.calendar_card, parent, false);
        return new CalendarView(v);
    }




    // onBindViewHolder is also an overriden method that handles new view bindings to the recyclerview
    @Override
    public void onBindViewHolder(CalendarView holder, int position) {
        Calendar calendar = this.calendar.get(position);
        // Set up the fields we want
        holder.entryTime.setText(calendar.time);
        holder.entryName.setText(calendar.name);
    }




    // getItemCount does what it says
    @Override
    public int getItemCount() {return calendar.size();}





    // ViewHolder class contains the single item being used to hold all our information
    // see: res/layout/calendar_card.xml
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
