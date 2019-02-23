package com.nsb.visions.varun.mynsb.Events;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsb.visions.varun.mynsb.Common.Util;
import com.nsb.visions.varun.mynsb.R;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by varun on 22/01/2018
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> events = new ArrayList<>();
    private Context context;




    // Constructor
    public EventAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }




    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.event_card, parent, false);
        return new EventViewHolder(v);
    }




    // onBindViewHolder setups the event card used to hold all our data in the recyclerview, see res/layout/event_card.xml
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);

        holder.eventName.setText(event.eventName);
        holder.shortDesc.setText(event.eventShortDesc);
        holder.longDesc.setText(event.eventLongDesc);
        setImage(event, holder.eventImage);

        String eventStart = Util.formateDate(event.eventStart, "dd/MM/yy");
        String eventEnd = Util.formateDate(event.eventEnd, "dd/MM/yy");

        // Set the timeholder
        holder.timeData.setText(eventStart + " - " + eventEnd);
        // On click listener for the readMore button
        holder.readMore.setOnClickListener((v) -> {
            String message = String.format(Locale.ENGLISH, "Created By: %s", event.eventOrganiser);
            Toast.makeText(this.context, message, Toast.LENGTH_LONG).show();
        });
    }




    @Override
    public int getItemCount() {return events.size();}




    // View holder class for the event instance, see res/layout/event_card.xml
    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView shortDesc;
        TextView longDesc;
        TextView timeData;
        ImageView eventImage;
        Button readMore;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventName  = itemView.findViewById(R.id.eventTitle);
            timeData = itemView.findViewById(R.id.timing);
            shortDesc  = itemView.findViewById(R.id.short_description);
            longDesc = itemView.findViewById(R.id.long_description);
            eventImage = itemView.findViewById(R.id.eventImage);
            readMore = itemView.findViewById(R.id.readMore);
        }
    }




    // setImage takes an imageView and an event and loads the event's image into the imageView
    private void setImage(Event event, ImageView imageView) {
        try {
            // Start encoding the URL
            URL url = new URL(event.eventPictureUrl);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();

            // Attain context
            Context context = imageView.getContext();


            // Load the image with picasso
            Picasso picasso = Picasso.with(context);
            picasso.setLoggingEnabled(true);
            picasso.load(url.toString())
                .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
