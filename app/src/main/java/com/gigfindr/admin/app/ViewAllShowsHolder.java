package com.gigfindr.admin.app;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import java.util.List;

/**
 * Created by admin on 8/5/17.
 */

public class ViewAllShowsHolder extends ParallaxViewHolder{

    protected TextView tvBandName, tvLocation, tvAddress, tvDate, tvTime, tvEntryFee, tvGenre;
    public Context context;

    public ViewAllShowsHolder(View itemview, List<ShowDetails> list, Context context){
        super(itemview);

        this.context = context;

        tvBandName = itemview.findViewById(R.id.allshowlist_bandName);
        tvLocation = itemview.findViewById(R.id.allshowlist_locationName);
        tvDate = itemview.findViewById(R.id.allshowlist_date);
        tvEntryFee = itemview.findViewById(R.id.allshowlist_fee);
        tvTime = itemview.findViewById(R.id.allshowlist_time);
        tvAddress = itemview.findViewById(R.id.allshowlist_address);

    }

    @Override
    public int getParallaxImageId() {
        return R.id.allshowlist_iv;
    }
}
