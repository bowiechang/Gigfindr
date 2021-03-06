package com.gigfindr.admin.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

//import com.google.android.gms.location.places.GeoDataClient;
//import com.google.android.gms.location.places.PlacePhotoMetadata;
//import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
//import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
//import com.google.android.gms.location.places.PlacePhotoResponse;
//import com.google.android.gms.location.places.Places;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.google.android.libraries.places.compat.GeoDataClient;
//import com.google.android.libraries.places.compat.Place;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.libraries.places.compat.PlacePhotoMetadata;
//import com.google.android.libraries.places.compat.PlacePhotoMetadataBuffer;
//import com.google.android.libraries.places.compat.PlacePhotoMetadataResponse;
//import com.google.android.libraries.places.compat.PlacePhotoResponse;
//import com.google.android.libraries.places.compat.Places;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

/**
 * Created by admin on 20/7/17.
 */

public class ViewMyShowsAdapter extends RecyclerView.Adapter<ViewMyShowsHolder> {

    protected List<ShowDetails> list;
    protected Context context;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//    protected GeoDataClient mGeoDataClient;

    public ViewMyShowsAdapter(Context context, List<ShowDetails> list){
        this.context = context;
        this.list = list;

//        mGeoDataClient = Places.getGeoDataClient(context, null);
//        mGeoDataClient = Places.getGeoDataClient(context);
    }

    @Override
    public ViewMyShowsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewMyShowsHolder viewMyShowsHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.allshows_list2, parent, false);
        View view = layoutInflater.inflate(R.layout.myshows_list, parent, false);
        viewMyShowsHolder = new ViewMyShowsHolder(view, list, context);

        return viewMyShowsHolder;
    }

    @Override
    public void onBindViewHolder(final ViewMyShowsHolder holder, final int position) {


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

        String[] monthValue = {"JAN","FEB","MAR", "APR", "MAY", "JUN", "JLU", "AUG", "SEP", "OCT", "NOV", "DEC"};

        String monthval = list.get(position).getDate();
        String[] split = monthval.split("/");

        int month = Integer.parseInt(split[1]);

        String date = split[0].concat(" ").concat(monthValue[(month-1)]).concat(" " + split[2]);


        //setTime

        String time = list.get(position).getStartTime().concat(" - " + list.get(position).getEndTime());

        holder.tvLocation.setText(list.get(position).getLocationName());
        holder.tvAddress.setText(list.get(position).getAddress());
        holder.tvDate.setText(split[0]);
        holder.tvDateMonth.setText(monthValue[(month-1)]);
        holder.tvTime.setText(time);

//        //reading of image
//        final StorageReference pathref = storageReference.child("userid"+ list.get(position).getUserid() +"/pic.jpg");
//        pathref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//
//                // Load the image using Glide
//                Glide.with(context)
//                        .load(uri.toString())
//                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                        .into(holder.getBackgroundImage());
//
//            }
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("failed uri", "failed: " + e.toString());
//            }
//        });

        getPhotos(holder, list.get(position).getPlaceid());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, PostShowActivity.class);
                Bundle extras = new Bundle();
                extras.putString("starttime", list.get(position).getStartTime());
                extras.putString("date", list.get(position).getDate());
                extras.putString("FROM_ACTIVITY", "viewmyshowadap");
                intent.putExtras(extras);
                context.startActivity(intent);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                Toasty.normal(context, "Sharing gig..", Toast.LENGTH_SHORT).show();

                String bandname = capitalize(list.get(position).getBandName());
                String[] split = list.get(position).getAddress().split(",");
                String locationaddress = list.get(position).getLocationName().concat(" (" + split[0] + ")");
                String time = list.get(position).getStartTime().concat(" to ").concat(list.get(position).getEndTime());
                String[] split2 = list.get(position).getDate().split("/");
                String[] monthValue = {"January","February","March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                int month = Integer.parseInt(split2[1]);
                String dateformated = split2[0] + " " + monthValue[(month-1)];
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "GIGFINDR'S GIG INVITATION");
                String string = "*GIGFINDR'S GIG SHARING*\n\n" +
                        "Hello, we are " + bandname + "!\n\n" + "We are having a gig \nat " + locationaddress + " \non " + dateformated + ", " + time + "! \n\nHope to see you there!" +
                        "\n\nFind us and many other live bands at GigFindr now! Link: https://play.google.com/store/apps/details?id=" + context.getPackageName() ;
                intent.putExtra(Intent.EXTRA_TEXT, string);
                context.startActivity(Intent.createChooser(intent, "Share with"));

                return true;
            }
        });

    }

    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }


    private void getPhotos(final ViewMyShowsHolder holder2, String placeid) {
//        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeid);
//        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
//                // Get the list of photos.
//                PlacePhotoMetadataResponse photos = task.getResult();
//                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
//                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
//
//                if(photoMetadataBuffer.getCount() > 0) {
//                    // Get the first photo in the list.
//                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
//                    // Get the attribution text.
////                CharSequence attribution = photoMetadata.getAttributions();
//                    // Get a full-size bitmap for the photo.
//                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
//                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
//                        @Override
//                        public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
//                            PlacePhotoResponse photo = task.getResult();
//                            Bitmap bitmap = photo.getBitmap();
//
////                            holder2.ivPlacePic.setImageBitmap(bitmap);
//
//
//                            Glide.with(context)
//                                    .load(bitmap)
//                                    .into(holder2.ivPlacePic);
//                        }
//                    });
//
//                }
//                else{
//                    int number = new Random().nextInt((3 - 1) + 1) + 1;
//
//                    int id = context.getResources().getIdentifier("drawable/" + "defaultplace" + number, null, context.getPackageName());
//                    holder2.ivPlacePic.setImageResource(id);
//
////                    Glide.with(context)
////                            .load(id)
////                            .into(holder2.ivPlacePic);
//
//
//                }
//            }
//        });






        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);

        // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(placeid, fields).build();
        Places.initialize(context, context.getString(R.string.google_api_key));
        final PlacesClient placesClient = Places.createClient(context);
        placesClient.fetchPlace(placeRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                Place place = fetchPlaceResponse.getPlace();
                if(place.getPhotoMetadatas() == null){
                    int number = new Random().nextInt((3 - 1) + 1) + 1;
//
                    int id = context.getResources().getIdentifier("drawable/" + "defaultplace" + number, null, context.getPackageName());
                    holder2.ivPlacePic.setImageResource(id);

//                    Glide.with(context)
//                            .load(id)
//                            .into(holder2.ivPlacePic);
                }
                else {

                    // Get the photo metadata.
                    PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);

                    // Get the attribution text.
                    String attributions = photoMetadata.getAttributions();

                    // Create a FetchPhotoRequest.
                    FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .build();

                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
                        @Override
                        public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {
                            Bitmap bitmap = fetchPhotoResponse.getBitmap();
                            Glide.with(context)
                                    .load(bitmap)
                                    .into(holder2.ivPlacePic);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        });
    }

}
