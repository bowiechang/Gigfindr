package com.example.admin.beerandmusic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by admin on 20/7/17.
 */

public class ViewTheirShowsAdapter extends RecyclerView.Adapter<ViewTheirShowsHolder> {

    protected List<ShowDetails> list;
    protected Context context;

    String bandName;

    public ViewTheirShowsAdapter(Context context, List<ShowDetails> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewTheirShowsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewTheirShowsHolder viewTheirShowsHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selectedshows_list, parent, false);
        viewTheirShowsHolder = new ViewTheirShowsHolder(view, list, context);

        return viewTheirShowsHolder;
    }

    @Override
    public void onBindViewHolder(ViewTheirShowsHolder holder, final int position) {
        holder.tvLocation.setText(String.format("Location: %s", list.get(position).getLocationName()));
        holder.tvAddress.setText(String.format("Address: %s", list.get(position).getAddress()));
        holder.tvDate.setText(String.format("Date: %s", list.get(position).getDate()));
        holder.tvEndTime.setText(String.format("End time: %s", list.get(position).getEndTime()));
        holder.tvEntryFee.setText(String.format("Entry Fee: %s", list.get(position).getEntryFee()));
        holder.tvStartTime.setText(String.format("Start time: %s", list.get(position).getStartTime()));
        holder.tvGenre.setText(String.format("Genre: %s", list.get(position).getGenre()));

        bandName = list.get(position).getBandName();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ViewDetailedShowActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name", list.get(position).getBandName());
                extras.putString("date", list.get(position).getDate());
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
