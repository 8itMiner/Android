package com.nsb.visions.varun.mynsb.FourU;

// 4U creation library


import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.amirs.JSON;
import jp.wasabeef.recyclerview.adapters.*;
import okhttp3.Request;
import okhttp3.Response;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;


public class FourU {
    private static ArticleAdapter adapter;

    // Function that loads an activity
    public static void LoadUI(RecyclerView rv, TextView errorHolder, Context context, Handler uiHandler) throws Exception {
        // Create a recyclerview to contain content


        // Send a http request to attain the 4U articles
        HTTP httpClient = new HTTP(context);
        // Set up a request for the client to send
        Request request = new Request.Builder()
                .get()
                .url("http://35.189.45.152:8080/api/v1/4U/Get")
                .build();

        // Startup the request thread
        Thread requestThread = new Thread(() -> {
            try {
                Response response = httpClient.performRequest(request);

                // Begin reading the json resp
                String jsonRaw = response.body().string();

                // Begin parsing that json and push it into the articles list
                JSON json = new JSON(jsonRaw);

                JSON bodyArray = json.key("Message").key("Body").index(0);

                // List of articles
                List<Article> articles = new ArrayList<>();


                // Iterate over json array and push it into the article list
                for(int i = 0; i < bodyArray.count(); i++) {
                    Article article = new Article(bodyArray.index(i).key("ArticleName").stringValue(),
                            bodyArray.index(i).key("ArticleDesc").stringValue(),
                            bodyArray.index(i).key("ArticleImageUrl").stringValue());

                    // Push into articles list
                    articles.add(article);
                }


                // Push the ui stuff into the handler
                uiHandler.post(() -> {
                    if (articles.isEmpty()) {
                        rv.setVisibility(View.GONE);
                        errorHolder.setVisibility(View.VISIBLE);
                    }

                    // Create adapter
                    adapter = new ArticleAdapter(articles);
                    ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(adapter);
                    alphaAdapter.setDuration(100);


                    rv.setAdapter(alphaAdapter);

                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        // Start the request thread
        requestThread.start();

    }

    public static String Get4U() {
        return "sum json";
    }
}


