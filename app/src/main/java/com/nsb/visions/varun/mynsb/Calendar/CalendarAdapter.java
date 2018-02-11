package com.nsb.visions.varun.mynsb.Calendar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by varun on 11/02/2018. Coz varun is awesome as hell :)
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarView> {

    public List<Calendar> calendars;

    public CalendarAdapter(List<Calendar> calendars) {
        this.calendars = calendars;
    }

    @Override
    public CalendarView onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CalendarView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    // View class
    class CalendarView extends RecyclerView.ViewHolder {

        public CalendarView(View itemView) {
            super(itemView);
        }
    }

}
