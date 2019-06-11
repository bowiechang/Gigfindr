package com.gigfindr.admin.app;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.google.common.eventbus.EventBus;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 10/5/17.
 */

public class MarkerInfoWindowAdapter implements InfoWindowAdapter {

    private View v = null;
    private LayoutInflater inflater = null;
    private LinearLayout gigContainer;

//    private HashMap<String, ShowDetails> detailsHashMap;
//    private HashMap<String, Map.Entry<Boolean, ShowDetails>> detailsHashMap;
    private DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference();
    private ArrayList windowInfoObjectArrayList;
    private ShowDetails showDetails;
    private Context context;



    //    MarkerInfoWindowAdapter(LayoutInflater inflater, HashMap<String, ShowDetails> hashMap){
    MarkerInfoWindowAdapter(LayoutInflater inflater, ArrayList<WindowInfoObject> windowInfoObjectArrayList, Context context){
        this.inflater = inflater;
        this.context = context;
//        this.detailsHashMap = hashMap;
        this.windowInfoObjectArrayList = windowInfoObjectArrayList;


    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {


        for (int i = 0; i < windowInfoObjectArrayList.size(); i++) {
            WindowInfoObject windowInfoObject = (WindowInfoObject) windowInfoObjectArrayList.get(i);
            if(windowInfoObject.getMarkerID().equalsIgnoreCase(marker.getId())) {
                //more than 1 show
                if(windowInfoObject.getMultipleShow()) {
                    if (v == null) {
                        v = inflater.inflate(R.layout.markerinfowindow_layout_multiple, null);
                    }
                    else{
                        v = null;
                        v = inflater.inflate(R.layout.markerinfowindow_layout_multiple, null);

                    }



                    TextView numberofShow = v.findViewById(R.id.numberOfShow);
                    TextView location = v.findViewById(R.id.location);
                    TextView address = v.findViewById(R.id.address);
                    TextView entryFee = v.findViewById(R.id.entryFee);


                    for (int j = 0; j < windowInfoObject.getShowDetailsArrayList().size(); j++) {

                        LayoutInflater layoutInflater = (LayoutInflater)this.context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = layoutInflater.inflate(R.layout.multiple_gig_row, null);

                        gigContainer = v.findViewById(R.id.showContainer);

                        showDetails = windowInfoObject.getShowDetailsArrayList().get(j);

                        TextView bandName = view.findViewById(R.id.bandName);
                        TextView numbering = view.findViewById(R.id.numbering);
                        TextView time = view.findViewById(R.id.time);


                        bandName.setText(capitalize(showDetails.getBandName()));
                        numbering.setText((j+1) + ". ");
                        time.setText("(" + showDetails.getStartTime() + " to " + showDetails.getEndTime() + ")");

                        gigContainer.addView(view);
                    }

                    if(showDetails.getEntryFee().equals("0") || showDetails.getEntryFee().equalsIgnoreCase("free")){
                        entryFee.setText(R.string.free_caps);
                    }
                    else{
                        entryFee.setText("$ " + showDetails.getEntryFee());
                    }


                    numberofShow.setText("Multiple gigs found at the location ");
                    location.setText(showDetails.getLocationName());
                    String[] splitaddress = showDetails.getAddress().split(",");
                    address.setText("("+splitaddress[0]+")");

                }
                else{
                    if (v == null) {
                        v = inflater.inflate(R.layout.markerinfowindow_layout, null);
                    }
                    else{
                        v = null;
                        v = inflater.inflate(R.layout.markerinfowindow_layout, null);

                    }

                    ConstraintLayout constraintLayout1 = v.findViewById(R.id.constraintLayout1);

                    TextView locationName = v.findViewById(R.id.locationName);
                    TextView address = v.findViewById(R.id.address);
                    TextView bandName = v.findViewById(R.id.bandName);
                    TextView genre = v.findViewById(R.id.genre);
                    TextView startTiming = v.findViewById(R.id.startTiming);
                    TextView endTiming = v.findViewById(R.id.endTiming);
                    TextView entryFee = v.findViewById(R.id.entryFee);
                    TextView date = v.findViewById(R.id.date);

                    for (int i2 = 0; i2 < windowInfoObjectArrayList.size(); i2++) {
                        WindowInfoObject windowInfoObject2 = (WindowInfoObject) windowInfoObjectArrayList.get(i2);
                        if(windowInfoObject2.getMarkerID().equalsIgnoreCase(marker.getId())){
                            for (int j = 0; j < windowInfoObject.getShowDetailsArrayList().size(); j++) {
                                ShowDetails showDetails = windowInfoObject.getShowDetailsArrayList().get(j);

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
                            }
                        }
                    }
                }

            }

        }

//        if (v == null) {
//            v = inflater.inflate(R.layout.markerinfowindow_layout, null);
//        }
//
//        ConstraintLayout constraintLayout1 = v.findViewById(R.id.constraintLayout1);
//
//        TextView locationName = v.findViewById(R.id.locationName);
//        TextView address = v.findViewById(R.id.address);
//        TextView bandName = v.findViewById(R.id.bandName);
//        TextView genre = v.findViewById(R.id.genre);
//        TextView startTiming = v.findViewById(R.id.startTiming);
//        TextView endTiming = v.findViewById(R.id.endTiming);
//        TextView entryFee = v.findViewById(R.id.entryFee);
//        TextView date = v.findViewById(R.id.date);
//
////        ShowDetails showDetails = detailsHashMap.get(marker.getId());
//
//        for (int i = 0; i < windowInfoObjectArrayList.size(); i++) {
//            WindowInfoObject windowInfoObject = (WindowInfoObject) windowInfoObjectArrayList.get(i);
//            if(windowInfoObject.getMarkerID().equalsIgnoreCase(marker.getId())){
//                for (int j = 0; j < windowInfoObject.getShowDetailsArrayList().size(); j++) {
//                    ShowDetails showDetails = windowInfoObject.getShowDetailsArrayList().get(j);
//
//                    locationName.setText(showDetails.getLocationName());
//                    bandName.setText(showDetails.getBandName());
//                    genre.setText(showDetails.getGenre());
//                    startTiming.setText(showDetails.getStartTime());
//                    endTiming.setText(showDetails.getEndTime());
//                    if(showDetails.getEntryFee().equals("0") || showDetails.getEntryFee().equalsIgnoreCase("free")){
//                        entryFee.setText(R.string.free_caps);
//                    }
//                    else{
//                        entryFee.setText("$ " + showDetails.getEntryFee());
//                    }
//
//                    String[] splitaddress = showDetails.getAddress().split(",");
//                    address.setText(splitaddress[0]);
//                }
//            }
//        }




        //code to add
//        View view = inflater.inflate(R.layout.markerinfowindow_layout, null);
//        TextView locationName2 = view.findViewById(R.id.locationName);
//        locationName2.setText(showDetails.getLocationName());
//        constraintLayout1.addView(view);


        return v;
    }

    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }
}
