package com.nsb.visions.varun.mynsb.Events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsb.visions.varun.mynsb.R;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by varun on 22/01/2018. Coz varun is awesome as hell :)
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    public List<Event> events = new ArrayList<>();
    private Context context;

    // Constructor
    public EventAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
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
        holder.longDesc.setText(event.eventLongDesc);
        // Set the image into the eventImage box
        setImage(event, holder.eventImage);

        // Base format
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        String start = formatter.format(event.eventStart);
        String end = formatter.format(event.eventEnd);

        holder.timeData.setText(start + " - " + end);

        holder.readMore.setOnClickListener((v) -> {
            String message = String.format(Locale.ENGLISH, "Created By: %s", event.eventOrganiser);
            Toast.makeText(this.context, message, Toast.LENGTH_LONG).show();
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }
     /*
        End Methods that must be overridden ==================================
     */


    // View holder class for the event instance
    @SuppressWarnings("ALL")
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





    /*
        UTILITY METHODS ===============================
     */
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
    /*
        END UTILITY METHODS ===============================
     */

}
