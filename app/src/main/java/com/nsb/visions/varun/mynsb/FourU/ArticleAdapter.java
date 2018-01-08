package com.nsb.visions.varun.mynsb.FourU;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleView> {

    private static List<Article> articles;


    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }



    class ArticleView extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView mainDesc;
        public ImageView backdrop;


        public ArticleView(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.editionName);
            mainDesc = (TextView) itemView.findViewById(R.id.description);
            backdrop = (ImageView) itemView.findViewById(R.id.imageBanner);
        }
    }

    public void clearData() {
        int size = articles.size();
        articles.clear();
        notifyItemRangeRemoved(0, size);
    }


    @Override
    public ArticleView onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.four_u_card, parent, false);

        return new ArticleView(v);
    }

    @Override
    public void onBindViewHolder(ArticleView holder, int position) {
        Article article = articles.get(position);
        holder.mainDesc.setText(article.LongDesc);
        holder.title.setText(article.name);
        try {
            Uri uri = Uri.parse(article.ImageURL);
            // Attain context
            Context context = holder.backdrop.getContext();
            // Load the image with picasso
            Picasso.with(context).load(uri)
                    .into(holder.backdrop);
        } catch (Exception e) {
            
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
