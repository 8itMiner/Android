package com.nsb.visions.varun.mynsb.FourU;

// 4U creation library


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import jp.wasabeef.recyclerview.adapters.*;



public class Four_U {
    public static ArticleAdapter adapter;

    // Function that loads an activity
    public static void LoadUI(RecyclerView rv, TextView errorHolder) {
        // Create a recyclerview to contain content

        // List of articles
        List<Article> articles = new ArrayList<Article>();
        articles.add(new Article("July", "Read all about the july edition!! This will be awesome", "https://s3.envato.com/files/44a75f36-9037-4fee-8473-5bb3d9157706/inline_image_preview.jpg"));
        articles.add(new Article("July", "Read all about the july edition!! This will be awesome", "https://i.ytimg.com/vi/VQTXl8wXXdM/maxresdefault.jpg"));
        articles.add(new Article("July", "Read all about the july edition!! This will be awesome", "http://wallpaper.pickywallpapers.com/samsung-epic/preview/snowy-peaks-and-pine-trees.jpg"));
        articles.add(new Article("July", "Read all about the july edition!! This will be awesome", "https://img00.deviantart.net/3a34/i/2013/139/a/3/epic_mountain_dragon_by_jjpeabody-d65u199.jpg"));

        if (articles.isEmpty()) {
            rv.setVisibility(View.GONE);
            errorHolder.setVisibility(View.VISIBLE);
        }

        // Create adapter
        adapter = new ArticleAdapter(articles);
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(adapter);
        alphaAdapter.setDuration(100);


        rv.setAdapter(alphaAdapter);
    }

    public static String Get4U() {
        return "sum json";
    }
}


