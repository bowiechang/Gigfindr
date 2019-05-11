package com.gigfindr.admin.app;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.google.common.eventbus.EventBus;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by admin on 10/5/17.
 */

public class MarkerInfoWindowAdapter implements InfoWindowAdapter {

    private View v = null;
    private LayoutInflater inflater = null;

    private HashMap<String, ShowDetails> detailsHashMap = new HashMap<>();
    private DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference();



    MarkerInfoWindowAdapter(LayoutInflater inflater, HashMap<String, ShowDetails> hashMap){
        this.inflater = inflater;
        this.detailsHashMap = hashMap;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        if (v == null) {
            v = inflater.inflate(R.layout.markerinfowindow_layout, null);
        }

        TextView locationName = v.findViewById(R.id.locationName);
        TextView address = v.findViewById(R.id.address);
        TextView bandName = v.findViewById(R.id.bandName);
        TextView genre = v.findViewById(R.id.genre);
        TextView startTiming = v.findViewById(R.id.startTiming);
        TextView endTiming = v.findViewById(R.id.endTiming);
        TextView entryFee = v.findViewById(R.id.entryFee);
        TextView date = v.findViewById(R.id.date);

        ShowDetails showDetails = detailsHashMap.get(marker.getId());

        locationName.setText(showDetails.getLocationName());
        bandName.setText(showDetails.getBandName());
        genre.setText(showDetails.getGenre());
        startTiming.setText(showDetails.getStartTime());
        endTiming.setText(showDetails.getEndTime());
        if(showDetails.getEntryFee().equals("0") || showDetails.getEntryFee().equalsIgnoreCase("free")){
            entryFee.setText(R.string.free_caps);
        }
        else{
            entryFee.setText("$ " + showDetails.getEntryFee());
        }

        String[] splitaddress = showDetails.getAddress().split(",");
        address.setText(splitaddress[0]);


        return v;
    }
}
