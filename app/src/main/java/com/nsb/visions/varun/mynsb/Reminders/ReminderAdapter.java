package com.nsb.visions.varun.mynsb.Reminders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderHolder> {

    private List<Reminder> reminders;


    public ReminderAdapter(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    @Override
    public ReminderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ReminderHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    // Reminder holder class
    public class ReminderHolder extends RecyclerView.ViewHolder {
        public ReminderHolder(View itemView) {
            super(itemView);
        }
    }

}
