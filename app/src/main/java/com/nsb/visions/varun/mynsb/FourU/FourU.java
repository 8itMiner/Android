package com.nsb.visions.varun.mynsb.FourU;

// 4U creation library


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import eu.amirs.JSON;
import okhttp3.Request;
import okhttp3.Response;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;
import com.nsb.visions.varun.mynsb.Common.Loader;

public class FourU extends Loader<Article> {


    public FourU(Context context) {
        super(context, FourU.class);
    }

    @Override
    public Article parseJson(JSON jsonD, int position) {
        JSON json = jsonD.index(position);

        return new Article(json.key("Name").stringValue(),
            json.key("Desc").stringValue(),
            json.key("ImageUrl").stringValue(), json.key("Link").stringValue());
    }

    @Override
    public RecyclerView.Adapter getAdapterInstance(List<Article> articles) {
        return new ArticleAdapter(articles, this.context);
    }

    @Override
    public Response sendRequest() {
        HTTP httpClient = new HTTP(context);



        // Set up a request for the client to send
        Request request = new Request.Builder()
            .get()
            .url(HTTP.API_URL + "/4U/get")
            .header("Connection", "close")
            .build();

        try {
            return httpClient.performRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


