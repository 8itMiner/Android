package com.nsb.visions.varun.mynsb.Events;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.nsb.visions.varun.mynsb.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by varun on 22/01/2018. Coz varun is awesome as hell :)
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    public List<Event> events;

    // Constructor
    public EventAdapter(List<Event> events) {
        this.events = events;
    }





    /*
        Methods that must be overridden ==================================
        documentation for overridden methods is minimal pleas check the android developer documentation
        for more information
     */
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.event_card, parent, false);

        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = events.get(position);
        // Start setting the text fields
        holder.eventName.setText(event.eventName);
        holder.shortDesc.setText(event.eventShortDesc);
        // Set the image into the eventImage box
        setImage(event, holder.eventImage);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
     /*
        End Methods that must be overridden ==================================
     */


    // View holder class for the event instance
    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView shortDesc;
        ImageView eventImage;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventName  = (TextView) itemView.findViewById(R.id.eventTitle);
            shortDesc  = (TextView) itemView.findViewById(R.id.description);
            eventImage = (ImageView) itemView.findViewById(R.id.eventImage);
        }
    }





    /*
        UTILITY METHODS ===============================
     */
    private void setImage(Event event, ImageView imageView) {
        Uri uri = Uri.parse(event.eventPictureUrl);
        // Attain context
        Context context = imageView.getContext();

        // Setup picasso
        Picasso picasso =  new Picasso.Builder(context)
            .downloader(new OkHttp3Downloader(context))
            .build();


        // Load the image with picasso
        picasso.with(context)
            .load(uri)
            .into(imageView);
    }
    /*
        END UTILITY METHODS ===============================
     */

}
