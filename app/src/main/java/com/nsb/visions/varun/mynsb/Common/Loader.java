package com.nsb.visions.varun.mynsb.Common;

import android.annotation.SuppressLint;
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

import com.nsb.visions.varun.mynsb.R;
import com.nsb.visions.varun.mynsb.Reminders.Reminders;
import com.nsb.visions.varun.mynsb.Timetable.Timetables;

import org.json.JSONObject;

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
    private Class extendedClass;



    /* constructor is just a constructor lmao
           @params;
               Context context
               Class extendedClass

    */
    public Loader(Context context, Class extendedClass) {
        this.context = context;
        this.extendedClass = extendedClass;
    }



    /* loadUI takes a recyclerView and an errorHolder it then loads all the models into the recyclerView, if there are none it displays the error message
        @params;
          RecyclerView rv
          SwipeRefreshLayout swiper
          ProgressBar progressBar
          TextView errorHolder
          Handler uiHandler
     */
    public void loadUI(RecyclerView rv, SwipeRefreshLayout swiper, ProgressBar progressBar, TextView errorHolder, Handler uiHandler) {

        // Determine if there is an internet connection
        if (!Util.isNetworkAvailable(this.context) && extendedClass != Reminders.class && extendedClass != Timetables.class) {
            showErrors(rv, errorHolder, true);
            progressBar.setVisibility(View.GONE);
            return;
        }

        // Set the visibility of the progressbar
        progressBar.setVisibility(View.VISIBLE);
        ProgressBar progressBar1 = progressBar;

        // Start a thread for http requests
        new Thread(() -> {
            // Set the required values
            this.swiper = swiper;
            this.errorHolder = errorHolder;
            this.uiHandler = uiHandler;

            // Startup the basic stuff for the recyclerView
            uiHandler.post(() -> {
                // Set the linearLayoutManager
                LinearLayoutManager llm = new LinearLayoutManager(this.context);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(llm);
            });

            // Get all the models
            List<Model> models = getModels();
            // Load the adapter into the recyclerView
            loadAdapter(rv, errorHolder, models, uiHandler);

            // Post it into the UI
            uiHandler.post(() -> {
                // Set the visibility of the progress bar
                progressBar.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                // Setup the refresher
                setupRefresher(rv);
            });
        }).start();
    }






    /*
    @ UTIL FUNCTIONS START =======================================
    */
    /* loadAdapter takes a recyclerView and loads an adapter into it
            @params;
                RecyclerView recyclerView
                TextView errorHolder
                List<Model> models
                Handler uiHandler
     */
    private void loadAdapter(RecyclerView recyclerView, TextView errorHolder, List<Model> models, Handler uiHandler) {
        // Post ui update to handler
        uiHandler.post(() -> {
            showErrors(recyclerView, errorHolder, models == null || models.isEmpty());

            if (models != null && !models.isEmpty()) {
                this.adapter = getAdapterInstance(models);
                int resId = R.anim.layout_animation_fall_down;
                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this.context, resId);
                recyclerView.setLayoutAnimation(animation);


                // Set the adapter
                recyclerView.setAdapter(this.adapter);
            }
        });
    }



    /* showErrors determine if error should be exposed to a user or not
            @params;
                List<Model> models
                RecyclerView recyclerView
                TextView errorHolder
     */
    public static void showErrors(RecyclerView recyclerView, TextView errorHolder, boolean errorOrNot) {
        // Article are empty so show the error message
        if (errorOrNot) {
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
                RecyclerView recyclerView
     */
    private void setupRefresher(RecyclerView recyclerView) {
        assert this.swiper != null;
        this.swiper.setOnRefreshListener(() -> {
            // Start a thread for loading data
            new Thread(() -> {
                List<Model> models = getModels();

                // Determine if errors need to be shown
                this.uiHandler.post(() -> {
                    showErrors(recyclerView, this.errorHolder, models == null || models.isEmpty());
                    // Setup the adapter
                    setUpAdapter(recyclerView, models);
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




    /*
        @UTIL FUNCTIONS =======================================
   */
    /* setUpAdapter sets up a recyclerView adapter for a swipeRefresh
            @params;
                RecyclerView recycler,
                List<Model> models
     */
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



    /* getModels retrieves all models from the API using the sendRequest function
        @params;
            nil

    */
    @SuppressLint("all")
    public List<Model> getModels() {
        // List of models
        List<Model> models = new ArrayList<>();

        // Get the response from the httpClient
        try {
            Response response = sendRequest();
            // Begin reading the json resp
            assert response != null;
            String jsonRaw = response.body().string();

            // Begin parsing that json and push it into the models list
            JSON json = new JSON(jsonRaw);
            // Get our body array with all the information
            JSON bodyArray = json.key("Message").key("Body").index(0);

            // Calculate the number of periods in the day based on the first period, if the first period is 0 that means that they have a period 0
            // And that there is 7 periods in the day

            int noPeriods = bodyArray.index(0).key("Period").intValue() == 0 ? 7 : 6;

            // FREE PERIOD CREATION
            addFreePeriods(bodyArray);
            // Add the free periods to our JSON object, this just reduces complexity down the line



            // Determine if a method has been overridden for the base class
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





    private void addFreePeriods(JSON bodyArray) {
        // Iterate over json array and push it into the models list
        for (int i = 1; i > bodyArray.count(); i++) {
            // Number of frees
            int period = bodyArray.index(i).key("Period").intValue();
            int numFrees = period - bodyArray.index(i-1).key("Period").intValue();

            for (int j = 0; j < numFrees; j++) {
                // Create a JSON object to indert into our data holder
                JSON freePeriod = JSON.create(
                    JSON.dic(
                        "class", "10FREE A",
                        "day", 1,
                        "period", String.valueOf(period + j),
                        "room", "",
                        "teacher", "BAI"
                    )
                );

                // Insert this into our current JSON holder
                bodyArray.addWithIndex(freePeriod, i + j);
            }
        }
    }
    /*
        @ UTIL FUNCTIONS END =======================================
     */
}
