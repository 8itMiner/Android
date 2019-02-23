package com.nsb.visions.varun.mynsb.Timetable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TimetableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Subject> subjects = new ArrayList<>();
    private String recyclerTitle;
    private Context context;




    // Constructor
    TimetableAdapter(List<Subject> subjects, String title, Context context) {
        // Reverse the subjects so that roll call is first
        subjects.add(0, subjects.get(subjects.size()-1));
        subjects.remove(subjects.size()-1);
        // Add a text holder to the top of our subjects list, onBindViewHolder will know to display this differently through getItemViewType
        // Looks like we are requesting this in the expanded view so we need to hide the title
        if (title != null) {
            subjects.add(0, new Subject(title, "", "", "", ""));
            this.recyclerTitle = title;
        }
        this.subjects = subjects;
        this.context = context;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0 && this.recyclerTitle != null) {
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
    public int getItemViewType(int position) {return position;}




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Week titles are that strip of text at the top of the screen, this strip of text tells us what day it is and the week
        // It looks like a week title
        // The adapter will automatically set the week title to be the 0th index in the model list
        if (getItemViewType(position) == 0 && recyclerTitle != null) {
            Subject subject = subjects.get(position);
            WeekTitle weekTitle = (WeekTitle) holder;
            weekTitle.weekTitle.setText(subject.className);
        } else {
            // Regular subject view, these are your typical subject cards... see: res/layout/timetable_card.xml
            // Get the timetable that we are talking about first
            Subject subject = subjects.get(position);
            SubjectView subjectView = (SubjectView) holder;
            try {
                // Attain the subject name
                subjectView.subject.setText(com.nsb.visions.varun.mynsb.Timetable.Util.getClassName(subject.className, this.context));
            } catch (IOException e) {
                e.printStackTrace();
            }
            subjectView.room.setText(subject.room);
            subjectView.teacher.setText(subject.teacher);
            subjectView.period.setText(subject.period);
            subjectView.time.setText(subject.time);
        }
    }




    @Override
    public int getItemCount() {return subjects.size();}




    // Holder class for holding our view and the general details regarding our ui
    // see: res/layout/timetable_card.xml
    public class SubjectView extends RecyclerView.ViewHolder {
        public TextView subject;
        TextView room;
        TextView teacher;
        TextView period;
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
    // Weektitle class, this is the Title we have at the top of our timetable
    public class WeekTitle extends RecyclerView.ViewHolder {
        TextView weekTitle;

        WeekTitle(View itemView) {
            super(itemView);
            // Get the respective elements
            this.weekTitle = itemView.findViewById(R.id.dayTextTitle);
        }
    }
}
