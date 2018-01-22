package com.nsb.visions.varun.mynsb.FourU;


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

// Article adapter class to make it easier to display articles given a list of them

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleView> {

    // Article list
    private List<Article> articles;


    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }


    /*
        @ MUST OVERRIDE FUNCTIONS =====================================
     */
    @Override
    public void onBindViewHolder(ArticleView holder, int position) {
        Article article = articles.get(position);
        holder.mainDesc.setText(article.LongDesc);
        holder.title.setText(article.name);
        setImage(article, holder.backdrop);
    }


    @Override
    public ArticleView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.four_u_card, parent, false);


        return new ArticleView(v);
    }


    @Override
    public int getItemCount() {
        return articles.size();
    }
    /*
        @ END MUST OVERRIDE FUNCTIONS =====================================
     */





    /*
        @ UTILITY CLASSES =================================================
     */
    // Class articleView is a view holder, it contains all the required views for our article
    class ArticleView extends RecyclerView.ViewHolder {
        TextView title;
        TextView mainDesc;
        ImageView backdrop;

        // Construct
        ArticleView(View itemView) {
            super(itemView);
            // Associate our public variables with views in our layout holder
            title = (TextView) itemView.findViewById(R.id.editionName);
            mainDesc = (TextView) itemView.findViewById(R.id.description);
            backdrop = (ImageView) itemView.findViewById(R.id.imageBanner);
        }
    }
    /*
        @ END UTILITY CLASSES =================================================
     */





    /*
        @ UTIL FUNCTIONS =====================================================
     */
    /* clearData clears all articles currently in our list it then notifies that adapter than stuff has been deleted
        @params;
            nil
     */
    public void clearData() {
        int size = articles.size();
        articles.clear();
        notifyItemRangeRemoved(0, size);
    }


    /* setImage takes an article and an imageView it then reads the image URL from the article and sets the image view to that image
        @params;
            Article article
            ImageView imageView
     */
    private void setImage(Article article, ImageView imageView) {
        Uri uri = Uri.parse(article.ImageURL);
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
        @ END UTIL FUNCTIONS =====================================================
     */
}
