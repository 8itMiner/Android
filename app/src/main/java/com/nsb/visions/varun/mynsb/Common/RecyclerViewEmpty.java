package com.nsb.visions.varun.mynsb.Common;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by varun on 6/12/2018. Coz varun is awesome as hell :)
 */

public class RecyclerViewEmpty extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
