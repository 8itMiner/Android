package com.nsb.visions.varun.mynsb.FourU;

// 4U creation library


import android.content.Context;

import java.util.List;

import eu.amirs.JSON;
import okhttp3.Request;
import okhttp3.Response;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;
import com.nsb.visions.varun.mynsb.Common.Loader;

public class FourU extends Loader<Article> {


    public FourU(Context context) {
        super(context);
    }

    @Override
    public Article parseJson(JSON json) {
        return new Article(json.key("ArticleName").stringValue(),
            json.key("ArticleDesc").stringValue(),
            json.key("ArticleImageUrl").stringValue());
    }

    @Override
    public void getAdapterInstance(List<Article> articles) {
        this.adapter = new ArticleAdapter(articles);
    }

    @Override
    public Response sendRequest() throws Exception {
        final Response[] response = new Response[1];
        // Thread for sending request
        Thread perfReq = new Thread(() -> {
            // Set up client
            HTTP httpClient = new HTTP(context);
            // Set up a request for the client to send
            Request request = new Request.Builder()
                .get()
                .url("http://35.189.45.152:8080/api/v1/4U/Get")
                .build();

            try {
                // Set the variable
                response[0] = httpClient.performRequest(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // Start request
        perfReq.start();
        // Return it
        return response[0];
    }
}


