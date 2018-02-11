package com.nsb.visions.varun.mynsb.Timetable;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.R;

import java.util.List;


public class TimetableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Subject> subjects;

    public TimetableAdapter(List<Subject> subjects, String title) {
        // Reverse the subjects so that roll call is first
        subjects.add(0, subjects.get(subjects.size()-1));
        subjects.remove(subjects.size()-1);

        // Add a text holder to the top of our subjects list, onBindViewHolder will know to display this differently through getItemViewType
        subjects.add(0, new Subject(title, "", "", ""));

        this.subjects = subjects;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reycycler_text_header, parent, false);
                return new WeekTitle(v);
            default:
                v = LayoutInflater.from(parent.getContext())
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

        switch (getItemViewType(position)) {
            case 0:
                // Title View
                Subject subject = subjects.get(position);
                WeekTitle weekTitle = (WeekTitle) holder;

                // Set the value
                weekTitle.weekTitle.setText(subject.className);
                break;

            default:
                // Regular subject view

                // Get the timetable that we are talking about first
                subject = subjects.get(position);
                SubjectView subjectView = (SubjectView) holder;

                // Start setting the respective data
                subjectView.subject.setText(subject.className);
                subjectView.room.setText(subject.room);
                subjectView.teacher.setText(subject.teacher);
                subjectView.period.setText(subject.period);
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

        public SubjectView(View itemView) {
            super(itemView);

            // Get the respective elements that we require
            this.subject = (TextView) itemView.findViewById(R.id.subjectName);
            this.room = (TextView) itemView.findViewById(R.id.room);
            this.teacher = (TextView) itemView.findViewById(R.id.teacher);
            this.period = (TextView) itemView.findViewById(R.id.period);
        }
    }

    public class WeekTitle extends RecyclerView.ViewHolder {

        public TextView weekTitle;

        public WeekTitle(View itemView) {
            super(itemView);

            // Get the respective elements
            this.weekTitle = (TextView) itemView.findViewById(R.id.dayTextTitle);
        }
    }
}
