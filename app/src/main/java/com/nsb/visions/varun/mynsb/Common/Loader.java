package com.nsb.visions.varun.mynsb.Common;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.R;

import java.util.ArrayList;
import java.util.List;

import eu.amirs.JSON;
import okhttp3.Response;

/**
 * Created by varun on 18/01/2018. Coz varun is awesome  :)
 */

// Loader class for loading data into a recyclerView
public abstract class Loader<Model> {

    protected RecyclerView.Adapter adapter;
    protected Context context;
    private SwipeRefreshLayout swiper;
    private TextView errorHolder;
    private Handler uiHandler;

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
    public void loadUI(RecyclerView rv, SwipeRefreshLayout swiper, ProgressBar progressBar, TextView errorHolder, Handler uiHandler) {
        // Set the required values
        this.swiper = swiper;
        this.errorHolder = errorHolder;
        this.uiHandler = uiHandler;

        // Startup the basic stuff for the recyclerView
        // Set the linearLayoutManager
        LinearLayoutManager llm = new LinearLayoutManager(this.context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        // Startup a thread to create our recyclerView
        // Get the list of all articles that the API can serve
        Thread loader = new Thread(() -> {
            try {
                List<Model> models = getModels();
                // Load the adapter into the recyclerView
                loadAdapter(rv, errorHolder, models, uiHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        loader.start();
        // Join the thread to prevent collision
        try {
            loader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Set the visibility of the progress bar
        progressBar.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);


        setupRefresher(rv);
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
            showErrors(models, recyclerView, errorHolder);

            this.adapter = getAdapterInstance(models);
            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this.context, resId);
            recyclerView.setLayoutAnimation(animation);


            // Set the adapter
            recyclerView.setAdapter(this.adapter);
        });
    }



    /* showErrors determine if error should be exposed to a user or not
            @params;
                List<Model> models
                RecyclerView recyclerView
                TextView errorHolder
     */
    private void showErrors(@NonNull List<Model> models, RecyclerView recyclerView, TextView errorHolder) {
        // Article are empty so show the error message
        if (models.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            errorHolder.setVisibility(View.VISIBLE);
        } else {
            // Hide the error holder in case it was always it is shown
            errorHolder.setVisibility(View.GONE);
        }
    }


    /* setup refresher sets up the refresher layout and attaches a swipe listener to it
        it adds a refresher which allows the user to load the models again
            @params;
                SwipeRefreshLayout refresher
                RecyclerView recyclerView
     */
    private void setupRefresher(RecyclerView recyclerView) {
        this.swiper.setOnRefreshListener(() -> {
            new Thread(() -> {
                // Get the model data
                List<Model> models = null;
                try {
                    models = getModels();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Determine if errors need to be shown
                assert models != null;
                showErrors(models, recyclerView, this.errorHolder);

                List<Model> finalModels = models;
                this.uiHandler.post(() -> {
                    // Setup the adapter
                    setUpAdapter(recyclerView, finalModels);

                    // Set the refresher
                    this.swiper.setRefreshing(false);
                    recyclerView.getAdapter().notifyDataSetChanged();
                });
            }).start();
        });
    }



    // Send request function must be overridden because it points to the URL we want
    abstract public Response sendRequest() throws Exception;
    // parseJson function must be overridden because it tells us how to get the models
    abstract public Model parseJson(JSON json) throws Exception;
    // get adapterInstance returns an instance of your adapter get adapter instance must set the value of our recycler adapter by returning an instance
    abstract public RecyclerView.Adapter getAdapterInstance(List<Model> models);





    /*
        @UTIL FUNCTIONS =======================================
   */

    /* setUpAdapter sets up a recyclerview adapter for a swipeRefresh
            @params;
                RecyclerView recycler,
                List<Model> models
     */
    private void setUpAdapter(RecyclerView recycler, List<Model> models) {
        // Set the models
        getAdapterInstance(models);
        recycler.setAdapter(this.adapter);

        // Start the animation again
        final Context context = recycler.getContext();
        final LayoutAnimationController controller =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recycler.setLayoutAnimation(controller);
        recycler.getAdapter().notifyDataSetChanged();
        recycler.scheduleLayoutAnimation();
    }


    /* getModels retrieves all models from the API
        @params;
            Context context

        @throws;
            Exception

    */
    private List<Model> getModels() throws Exception {
        // List of models
        List<Model> models = new ArrayList<>();

        // Get the response from the httpClient
        Response response = sendRequest();
        // Begin reading the json resp
        String jsonRaw = response.body().string();
        // Begin parsing that json and push it into the models list
        JSON json = new JSON(jsonRaw);
        // Get our body array with all the information
        JSON bodyArray = json.key("Message").key("Body").index(0);
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
