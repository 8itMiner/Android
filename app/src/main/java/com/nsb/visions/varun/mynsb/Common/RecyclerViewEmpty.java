package com.nsb.visions.varun.mynsb.Common;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by varun on 6/12/2018.
 */


// RecyclerViewEmpty is just a sample empty RecyclerView class, this acts as a template for an empty RecyclerView
public class RecyclerViewEmpty extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {return null;}
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){}
    @Override
    public int getItemCount() {return 0;}
}
