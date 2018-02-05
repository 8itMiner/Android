package com.nsb.visions.varun.mynsb.Common;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nsb.visions.varun.mynsb.R;

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
    public void loadUI(RecyclerView rv, SwipeRefreshLayout swiper, TextView errorHolder, Handler uiHandler) {
        // Startup the basic stuff for the recyclerView
        // Set the linearLayoutManager
        LinearLayoutManager llm = new LinearLayoutManager(this.context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        // Startup a thread to create our recyclerView
        Thread requestThread = new Thread(() -> {
            try {
                // Get the list of all articles that the API can serve
                List<Model> models = getModels();
                // Load the adapter into the recyclerView
                loadAdapter(rv, errorHolder, models, uiHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Start the request thread
        requestThread.start();
        setupRefresher(swiper, rv);
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

            this.adapter = getAdapterInstance(models);
            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this.context, resId);
            recyclerView.setLayoutAnimation(animation);


            // Set the adapter
            recyclerView.setAdapter(this.adapter);
        });
    }
    /* setup refresher sets up the refresher layout and attaches a swipe listener to it
        it adds a refresher which allows the user to load the models again
            @params;
                SwipeRefreshLayout refresher
                RecyclerView recyclerView
     */
    private void setupRefresher(SwipeRefreshLayout refresher, RecyclerView recyclerView) {
        refresher.setOnRefreshListener(
            () -> {
                // Reload the models into our adapter
                // Get the models
                List<Model> models = null;
                try {
                    models = getModels();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Set the models
                getAdapterInstance(models);
                recyclerView.setAdapter(this.adapter);

                // Start the animation again
                final Context context = recyclerView.getContext();
                final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

                recyclerView.setLayoutAnimation(controller);
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();

                refresher.setRefreshing(false);
            }
        );
    }



    // Send request function must be overridden because it points to the URL we want
    abstract public Response sendRequest() throws Exception;
    // parseJson function must be overridden because it tells us how to get the models
    abstract public Model parseJson(JSON json) throws Exception;
    // TODO: Figure out a better method to load the adapter instead of having someone else manually set it
    // get adapterInstance returns an instance of your adapter get adapter instance must set the value of our recycler adapter by returning an instance
    abstract public RecyclerView.Adapter getAdapterInstance(List<Model> models);





    /*
           @ UTIL FUNCTIONS =======================================
   */
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
            Log.d("Json-content", bodyArray.index(i).stringValue());
            models.add(parseJson(bodyArray.index(i)));
        }

        // Return the models
        return models;
    }
    /*
        @ UTIL FUNCTIONS END =======================================
     */
}
