package com.example.admin.beerandmusic;

import android.content.Context;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ViewMyShowsHolder extends RecyclerView.ViewHolder {

    protected TextView tvLocation, tvAddress, tvDate, tvDateMonth, tvTime;
    protected ImageView ivPlacePic;
    public Context context;

    public ViewMyShowsHolder(View itemview, List<ShowDetails> list, Context context){
        super(itemview);

        this.context = context;

        ivPlacePic = itemview.findViewById(R.id.ivPlacePic);
        tvAddress = itemview.findViewById(R.id.textViewAddress);
        tvTime = itemview.findViewById(R.id.textViewTime);
        tvLocation = itemview.findViewById(R.id.textViewlocationName);
        tvDate = itemview.findViewById(R.id.textViewDate);
        tvDateMonth = itemview.findViewById(R.id.textViewDateMonth);

    }
}
