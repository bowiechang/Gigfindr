package com.gigfindr.admin.app;

import android.content.Context;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ViewTheirShowsHolder extends RecyclerView.ViewHolder {

    protected TextView tvLocation, tvAddress, tvDate, tvStartTime, tvEndTime, tvEntryFee, tvGenre;
    public Context context;

    public ViewTheirShowsHolder(View itemview, List<ShowDetails> list, Context context){
        super(itemview);

        this.context = context;
        tvLocation = (TextView) itemview.findViewById(R.id.tvLocationName);
        tvAddress = (TextView) itemview.findViewById(R.id.tvAddress);
        tvDate = (TextView) itemview.findViewById(R.id.tvDate);
        tvStartTime = (TextView) itemview.findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) itemview.findViewById(R.id.tvEndTime);
        tvEntryFee = (TextView) itemview.findViewById(R.id.tvEntryFee);
        tvGenre = (TextView) itemview.findViewById(R.id.tvGenre);
    }
}
