package com.nsb.visions.varun.mynsb.Common;

// 19/01/2018

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class LoaderAdapter<Model> extends RecyclerView.Adapter<LoaderAdapter.ViewHolder> {

    private List<Model> models;
    private int viewID;

    // Constructor will take a list of models and the id of the view that will display our content
    public LoaderAdapter(List<Model> models, int viewID) {
        this.models = models;
        this.viewID = viewID;
    }

    @Override
    public abstract ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(LoaderAdapter.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return models.size();
    }

    // ViewHolder class must be overridden otherwise the adapter will fail to show the correct information
    protected class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
