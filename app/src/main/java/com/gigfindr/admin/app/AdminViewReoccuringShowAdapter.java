package com.gigfindr.admin.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 20/7/17.
 */

public class AdminViewReoccuringShowAdapter extends RecyclerView.Adapter<ViewAllShowsHolder> {

    protected List<ShowDetails> list;
    protected Context context;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public AdminViewReoccuringShowAdapter(Context context, List<ShowDetails> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewAllShowsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewAllShowsHolder viewAllShowsHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.allshows_list3, parent, false);
        viewAllShowsHolder = new ViewAllShowsHolder(view, list, context);

        return viewAllShowsHolder;
    }

    @Override
    public void onBindViewHolder(final ViewAllShowsHolder holder, final int position) {


        ////////////////////////////////////////////
        //getting date obj
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");

        String receivedDate = list.get(position).getDate();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        Date DateshowDate = null;
        Date DatetodayDate = null;

        try {
            DateshowDate = sdf.parse(receivedDate);
        }
        catch (Exception e){

        }
        //getting date obj


        String dayOfTheWeek = (String) DateFormat.format("EEEE", DateshowDate);
        ////////////////////////////////////////////

        String[] monthValue = {"January","February","March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        String monthval = list.get(position).getDate();
        String[] split = monthval.split("/");

        int month = Integer.parseInt(split[1]);

        String date = dayOfTheWeek.concat(" " + split[0].concat(" ").concat(monthValue[(month-1)]));
        String time = list.get(position).getStartTime().concat(" - ").concat(list.get(position).getEndTime());

        if(countChar(list.get(position).getAddress(), ',') > 1){
            String[] splitaddress = list.get(position).getAddress().split(",");
            String address = "("+splitaddress[0].trim()+")";
            String stringremove = address.replace(",","").replace(".","");
            holder.tvAddress.setText(stringremove);
        }
        else{
            String[] splitaddress = list.get(position).getAddress().split("Singapore");
            String address = "("+splitaddress[0].trim()+")";
            String stringremove = address.replace(",","").replace(".","");
            holder.tvAddress.setText(stringremove);
        }

        holder.tvLocation.setText(list.get(position).getLocationName());
        holder.tvBandName.setText(list.get(position).getBandName());
        holder.tvDate.setText(date);
        holder.tvTime.setText(time);

        if(list.get(position).getEntryFee().equalsIgnoreCase("free") || list.get(position).getEntryFee().equalsIgnoreCase("0")){
            holder.tvEntryFee.setText(R.string.free_caps);
        }
        else{
            holder.tvEntryFee.setText("$".concat(list.get(position).getEntryFee()));
        }

        //reading of image
        final StorageReference pathref = storageReference.child("userid"+ list.get(position).getUserid() +"/pic.jpg");
        pathref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Load the image using Glide
                Glide.with(context)
                        .load(uri.toString())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(true)
                        .into(holder.getBackgroundImage());

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Glide.with(context)
                        .load(R.drawable.bandpicblackwhite)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(true)
                        .into(holder.getBackgroundImage());
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AdminPostShowActivity.class);
                Bundle extras = new Bundle();
                extras.putString("uid", list.get(position).getUserid());
                extras.putString("location", list.get(position).getLocationName());
                extras.putString("latlng", list.get(position).getLatLng());
                extras.putString("starttime", list.get(position).getStartTime());
                extras.putString("endtime", list.get(position).getEndTime());
                extras.putString("entryfee", list.get(position).getEntryFee());
                extras.putString("bandname", list.get(position).getBandName());
                extras.putString("genre", list.get(position).getGenre());
                extras.putString("placeid", list.get(position).getPlaceid());
                extras.putString("address", list.get(position).getAddress());
                intent.putExtras(extras);
                context.startActivity(intent);

            }
        });

        holder.getBackgroundImage().reuse();

    }

    @Override
    public void onViewRecycled(@NonNull ViewAllShowsHolder holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    private int countChar(String str, char c){
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == c){
                count ++;
            }
        }
        return count;
    }

}
