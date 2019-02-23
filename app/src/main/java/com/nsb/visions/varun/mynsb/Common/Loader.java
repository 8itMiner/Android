package com.nsb.visions.varun.mynsb.Common;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nsb.visions.varun.mynsb.HTTP.HTTP;
import com.nsb.visions.varun.mynsb.R;

import java.util.ArrayList;
import java.util.List;

import eu.amirs.JSON;
import okhttp3.Response;

/**
 */

// Loader class for loading data into a recyclerView
public abstract class Loader<Model> {

    protected RecyclerView.Adapter adapter;
    protected Context context;
    private SwipeRefreshLayout swiper;
    private TextView errorHolder;
    private Handler uiHandler;


    // Constructor
    public Loader(Context context) {this.context = context;}




    // loadUI takes a recyclerView and an errorHolder it then loads all the models into the recyclerView, if there are none it displays the error message
    public void loadUI(RecyclerView rv, SwipeRefreshLayout swiper, ProgressBar progressBar, TextView errorHolder, Handler uiHandler) {

        if (!HTTP.validConnection(this.context)) {
            // Pop an error because there is no usable connection
            Util.showErrors(rv, errorHolder, true);
            progressBar.setVisibility(View.GONE);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            this.swiper = swiper;
            this.errorHolder = errorHolder;
            this.uiHandler = uiHandler;

            // Startup the basic stuff for the recyclerView
            uiHandler.post(() -> {
                LinearLayoutManager llm = new LinearLayoutManager(this.context);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(llm);
            });

            List<Model> models = getModels();
            loadAdapter(rv, errorHolder, models, uiHandler);

            // Set the visibility of the progress bar & setup the refresher
            uiHandler.post(() -> {
                progressBar.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                setupRefresher(rv);
            });
        }).start();

    }




    // loadAdapter takes a recyclerView and loads an adapter into it
    private void loadAdapter(RecyclerView recyclerView, TextView errorHolder, List<Model> models, Handler uiHandler) {
        uiHandler.post(() -> {
            if (models != null && !models.isEmpty()) {
                this.adapter = getAdapterInstance(models);
                int recylerviewAnimation = R.anim.layout_animation_fall_down;
                LayoutAnimationController refreshAnimation = AnimationUtils.loadLayoutAnimation(this.context, recylerviewAnimation);
                recyclerView.setLayoutAnimation(refreshAnimation);
                recyclerView.setAdapter(this.adapter);
            } else {
                Util.showErrors(recyclerView, errorHolder, true);
            }
        });
    }




    // setup refresher sets up the refresher layout and attaches a swipe listener to it
    private void setupRefresher(RecyclerView recyclerView) {
        assert this.swiper != null;
        this.swiper.setOnRefreshListener(() -> {
            new Thread(() -> {
                List<Model> models = getModels();

                // Display the new content
                this.uiHandler.post(() -> {
                    // Setup the adapter
                    setUpAdapter(recyclerView, models);
                    // Determine to show and error or not
                    Util.showErrors(recyclerView, this.errorHolder, models == null || models.isEmpty());
                    // Set the refresher
                    this.swiper.setRefreshing(false);
                    recyclerView.getAdapter().notifyDataSetChanged();
                });

            }).start();
        });
    }




    // Send request function must be overridden because it points to the URL we want
    abstract public Response sendRequest();
    // parseJson function must be overridden because it tells us how to get the models
    abstract public Model parseJson(JSON bodyBlock, int position) throws Exception;
    // get adapterInstance returns an instance of your adapter get adapter instance must set the value of our recycler adapter by returning an instance
    abstract public RecyclerView.Adapter getAdapterInstance(List<Model> models);




    // setUpAdapter sets up a recyclerView adapter given a certain recycler, note the recycler is parsed by reference
    private void setUpAdapter(RecyclerView recycler, List<Model> models) {
        // Set the models
        RecyclerView.Adapter adapter = getAdapterInstance(models);
        recycler.swapAdapter(adapter, true);

        // Start the animation again
        final Context context = recycler.getContext();
        final LayoutAnimationController controller =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recycler.setLayoutAnimation(controller);
        recycler.getAdapter().notifyDataSetChanged();
        recycler.scheduleLayoutAnimation();
    }




    // getModels retrieves all models from the API using the overloaded sendRequest function
    public List<Model> getModels() {
        List<Model> models = new ArrayList<>();

        // Begin the HTTP request
        try {
            String jsonResponseRaw = sendRequest().body().string();
            JSON jsonResponse = new JSON(jsonResponseRaw);
            JSON bodyArray = jsonResponse.key("Message").key("Body").index(0);
            // Add them to the models
            for (int i = 0; i < bodyArray.count(); i++) {
                models.add(parseJson(bodyArray, i));
            }
            return models;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the models
        return null;
    }
}
