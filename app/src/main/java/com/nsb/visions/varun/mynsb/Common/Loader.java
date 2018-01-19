package com.nsb.visions.varun.mynsb.Common;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.amirs.JSON;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import okhttp3.Response;

/**
 * Created by varun on 18/01/2018. Coz varun is awesome as hell :)
 */

// Loader class for loading data into a recyclerView
public abstract class Loader<Model> {

    protected RecyclerView.Adapter adapter;
    protected Context context;

    public Loader(Context context) {
        this.context = context;
    }




    /* loadUI takes a recyclerView and an errorHolder it then loads all the models into the recyclerView, if there are none it displays the error message
        @params;
          RecyclerView rv
          TextView errorHolder
          Context context
          Handler uiHandler

        @throws;
            Exception
     */
    public void loadUI(RecyclerView rv, TextView errorHolder, Handler uiHandler) throws Exception {
        // Startup a thread to create our recyclerView
        Thread requestThread = new Thread(() -> {
            try {
                // Get the list of all articles that the API can serve
                List<Model> articles = getModels();
                // Load the adapter into the recyclerView
                loadAdapter(rv, errorHolder, articles, uiHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Start the request thread
        requestThread.start();
    }






    /*
    @ UTIL FUNCTIONS START =======================================
 */
    /* loadAdapter takes a recyclerView and loads an adapter into it
            @params;
                RecyclerView recyclerView
                TextView errorHolder
                List<Article> articles
                Handler uiHandler

            @throws;
                Exception
     */
    private void loadAdapter(RecyclerView recyclerView, TextView errorHolder, List<Model> models, Handler uiHandler) throws Exception {
        // Post ui update to handler
        uiHandler.post(() -> {
            // Article are empty so show the error message
            if (models.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                errorHolder.setVisibility(View.VISIBLE);
            }

            getAdapterInstance(models);
            ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(this.adapter);
            alphaAdapter.setDuration(100);

            // Set the adapter
            recyclerView.setAdapter(alphaAdapter);
        });
    }



    // Send request function must be overridden because it points to the URL we want
    abstract public Response sendRequest() throws Exception;
    // parseJson function must be overridden because it tells us how to get the models
    abstract public Model parseJson(JSON json) throws Exception;
    // TODO: Figure out a better method to load the adapter instead of having someone else manually set it
    // get adapterInstance returns an instance of your adapter get adapter instance must set the value of our recycler adapter by returning an instance
    abstract public void getAdapterInstance(List<Model> models);





    /* getModels retrieves all models from the API
        @params;
            Context context

        @throws;
            Exception

 */
    private List<Model> getModels() throws Exception {
        // Get the response from the httpClient
        Response response = sendRequest();

        // Begin reading the json resp
        String jsonRaw = response.body().string();

        // Begin parsing that json and push it into the models list
        JSON json = new JSON(jsonRaw);

        // Get our body array with all the information
        JSON bodyArray = json.key("Message").key("Body").index(0);

        // List of models
        List<Model> models = new ArrayList<>();


        // Iterate over json array and push it into the models list
        for(int i = 0; i < bodyArray.count(); i++) {
            models.add(parseJson(bodyArray.index(i)));
        }

        // Return the models
        return models;
    }
    /*
        @ UTIL FUNCTIONS END =======================================
     */
}
