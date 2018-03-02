package com.nsb.visions.varun.mynsb.Timetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.HTTP.HTTP;
import com.nsb.visions.varun.mynsb.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eu.amirs.JSON;
import okhttp3.Request;


public class TimetableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Subject> subjects = new ArrayList<>();
    private JSON json;
    private String title;
    private Context context;

    public TimetableAdapter(List<Subject> subjects, String title, String dayStr, SharedPreferences preferences, Context context) {
        // Reverse the subjects so that roll call is first
        subjects.add(0, subjects.get(subjects.size()-1));
        subjects.remove(subjects.size()-1);

        // Add a text holder to the top of our subjects list, onBindViewHolder will know to display this differently through getItemViewType
        if (title != null) {
            subjects.add(0, new Subject(title, "", "", ""));
            this.title = title;
        }

        // Attain the bellTimes from the sharedPreferences
        String belltimes = preferences.getString("belltimes{data}", "");
        // Determine if there really is any data in the belltimes shared prefs if not then pull the data from the api
        if (belltimes.isEmpty()) {
            belltimes = getBelltimes(context);
            // push data into the shared preferences
            preferences.edit().putString("belltimes{data}", belltimes).apply();
        }

        // Parse the json
        this.json = new JSON(belltimes).key("body").index(0).key(dayStr);
        this.subjects = subjects;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0 && this.title != null) {
            View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reycycler_text_header, parent, false);
            return new WeekTitle(v);
        } else {
            // Doesn't have to be in an else it only here to make it easier to read
            View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timetable_card, parent, false);
            return new SubjectView(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == 0 && title != null) {
            // Title View
            Subject subject = subjects.get(position);
            WeekTitle weekTitle = (WeekTitle) holder;

            // Set the value
            weekTitle.weekTitle.setText(subject.className);
        } else {
            String time = getTime(position);
            // Regular subject view
            // Get the timetable that we are talking about first
            Subject subject = subjects.get(position);
            SubjectView subjectView = (SubjectView) holder;

            // Start setting the respective data
            try {
                // Attain the subject name
                subjectView.subject.setText(com.nsb.visions.varun.mynsb.Timetable.Util.getClassName(subject.className, this.context));
            } catch (IOException e) {
                e.printStackTrace();
            }
            subjectView.room.setText(subject.room);
            subjectView.teacher.setText(subject.teacher);
            subjectView.period.setText(subject.period);
            subjectView.time.setText(time);
        }
    }


    @Override
    public int getItemCount() {
        return subjects.size();
    }
    // Holder class for holding our view and the general details regarding our ui
    public class SubjectView extends RecyclerView.ViewHolder {

        public TextView subject;
        public TextView room;
        public TextView teacher;
        public TextView period;
        public TextView time;

        SubjectView(View itemView) {
            super(itemView);

            // Get the respective elements that we require
            this.subject = itemView.findViewById(R.id.subjectName);
            this.room = itemView.findViewById(R.id.room);
            this.teacher = itemView.findViewById(R.id.teacher);
            this.period = itemView.findViewById(R.id.period);
            this.time = itemView.findViewById(R.id.time);
        }
    }
    public class WeekTitle extends RecyclerView.ViewHolder {

        public TextView weekTitle;

        public WeekTitle(View itemView) {
            super(itemView);

            // Get the respective elements
            this.weekTitle = itemView.findViewById(R.id.dayTextTitle);
        }
    }
    // get belltimes data
    private String getBelltimes(Context context) {
        final String[] times = {""};
        // Start up a thread
        Thread requestThread = new Thread(() -> {
            HTTP http = new HTTP(context);

            // Setup a request
            Request request = new Request.Builder()
                .get()
                .url("35.189.45.152:8080/api/v1/belltimes/Get")
                .build();

            try {
                times[0] = http.performRequest(request).body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        requestThread.start();
        try {
            requestThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return times[0];
    }
    private String getTime(int position) {
        int incrementAmt = 2;
        // Determine the time
        String time = "8:50 - 8:58";
        // Get the time data from the json
        // Determine if the student has a period 0 or not
        if (subjects.size() == 9) {
            // They have a period 0
            incrementAmt = 3;
        }
        // Determine the amount to incrment
        // Figure out if the current period is roll call or not
        if (position != 2) {
            // Get the time for that period
            time = this.json.key(String.valueOf(position - incrementAmt)).stringValue();
        }
        return time;
    }
}
