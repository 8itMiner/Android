package com.nsb.visions.varun.mynsb.FourU;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import java.util.ArrayList;
import java.util.List;

// Article adapter class to make it easier to display articles given a list of them
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleView> {

    private List<Article> articles = new ArrayList<>();
    private Context context;




    // Constructor
    ArticleAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }




    // OnBindViewHolder is a quick function thats called whenever the recylerview updates
    @Override
    public void onBindViewHolder(@NonNull ArticleView holder, int position) {
        Article article = articles.get(position);
        holder.mainDesc.setText(article.LongDesc);
        holder.title.setText(article.name);
        setImage(article, holder.backdrop);
        // Set a click listener on the read button
        holder.readButton.setOnClickListener((v) -> {
            String articleURL = article.issuuLink;

            // Redirect them to chrome
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleURL));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.android.chrome");
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                // Chrome browser presumably not installed so allow user to choose instead
                Toast.makeText(context, "Chrome is not installed, to read this article please visit: " + articleURL, Toast.LENGTH_LONG).show();
            }
        });
        // Set the click listener for the download button
        holder.downlaodButton.setOnClickListener((v) -> {
            Toast.makeText(this.context, "Download feature not working as of yet", Toast.LENGTH_LONG).show();
        });
    }




    @NonNull
    @Override
    public ArticleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.four_u_card, parent, false);


        return new ArticleView(v);
    }




    @Override
    public int getItemCount() {return articles.size();}




    // Class articleView is a view holder, it contains all the required views for our article, see res/layout/4u_card.xml
    class ArticleView extends RecyclerView.ViewHolder {
        TextView title;
        TextView mainDesc;
        ImageView backdrop;
        Button readButton;
        Button downlaodButton;

        // Construct
        ArticleView(View itemView) {
            super(itemView);
            // Associate our public variables with views in our layout holder
            title = itemView.findViewById(R.id.editionName);
            mainDesc = itemView.findViewById(R.id.description);
            backdrop = itemView.findViewById(R.id.imageBanner);
            readButton = itemView.findViewById(R.id.readID);
            downlaodButton = itemView.findViewById(R.id.downloadID);
        }
    }




    // setImage takes an article and an imageView it then reads the image URL from the article and sets the image view to that image
    private void setImage(Article article, ImageView imageView) {
        try {
            // Start encoding the URL
            URL url = new URL(article.ImageURL);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            // Attain context
            Context context = imageView.getContext();
            // Load the image with picasso
            Picasso.with(context)
                .load(url.toString())
                .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
