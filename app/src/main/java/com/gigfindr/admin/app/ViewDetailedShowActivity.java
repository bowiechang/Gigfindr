package com.gigfindr.admin.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
//import com.google.android.gms.location.places.PlacePhotoMetadata;
//import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
//import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
//import com.google.android.gms.location.places.PlacePhotoResponse;
//import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.libraries.places.compat.GeoDataClient;
//import com.google.android.libraries.places.compat.PlacePhotoMetadata;
//import com.google.android.libraries.places.compat.PlacePhotoMetadataBuffer;
//import com.google.android.libraries.places.compat.PlacePhotoMetadataResponse;
//import com.google.android.libraries.places.compat.PlacePhotoResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class ViewDetailedShowActivity extends AppCompatActivity implements OnClickListener {

    private Toolbar toolbar;
    private ImageView imageView, imageViewMap;
    private TextView tvLocation, tvTime, tvDate, tvAboutBand, tvEntryFee, tvGenre, tvAddress;
    private RelativeLayout btnGetDirection, relativeLayoutDefaultPlaceCover, btnShare;

    private StorageReference storageReference;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Show");
    private DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("User");

    private String name, date, address, uid, instagramUid, instagramName;
    private ShowDetails showDetails;

    private RelativeLayout relativeLayoutSocialMediaInstagram;
    private RelativeLayout relativeLayoutBandTab, relativeLayoutShowTab, relativeLayoutShowTabBody, relativeLayoutDimmer;

//    protected GeoDataClient mGeoDataClient;

    private ValueEventListener valueEventListener1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailedshow_activity_redesign);

        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK);

//        mGeoDataClient = com.google.android.libraries.places.compat.Places.getGeoDataClient(this);

        TextView bandName = findViewById(R.id.tvBandName2);

        relativeLayoutShowTab = findViewById(R.id.relativeLayoutShowTab);
        relativeLayoutBandTab = findViewById(R.id.relativeLayoutBandTab);
        relativeLayoutShowTabBody = findViewById(R.id.relativeLayoutShow);
        relativeLayoutDimmer = findViewById(R.id.relativeLayoutBlackDim);
        relativeLayoutShowTabBody.setVisibility(View.INVISIBLE);
        relativeLayoutBandTab.setOnClickListener(this);
        relativeLayoutShowTab.setOnClickListener(this);
        relativeLayoutSocialMediaInstagram = findViewById(R.id.relativeLayoutSocialMediaInstagram);
        relativeLayoutSocialMediaInstagram.setOnClickListener(this);
        relativeLayoutSocialMediaInstagram.setVisibility(View.INVISIBLE);


        imageView = findViewById(R.id.imageViewBandpic);
        imageViewMap = findViewById(R.id.ivMapImg);
        tvLocation = findViewById(R.id.tvLocationName);
        tvAddress = findViewById(R.id.tvAddress);
        tvTime = findViewById(R.id.tvTime);
        tvDate = findViewById(R.id.tvDate);
        tvAboutBand = findViewById(R.id.tvAboutBand);
        tvEntryFee = findViewById(R.id.tvFee);
        tvGenre = findViewById(R.id.tvGenre);
        btnGetDirection = findViewById(R.id.buttonGetDirections);
        btnShare = findViewById(R.id.rlShareShow);
        relativeLayoutDefaultPlaceCover = findViewById(R.id.defaultImageCover);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        final RelativeLayout relativeLayout = findViewById(R.id.spinnerkitloader);
        btnGetDirection.setVisibility(View.INVISIBLE);

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                } // Just catch the InterruptedException

                // Now we use the Handler to post back to the main thread
                handler.post(new Runnable() {
                    public void run() {
                        // Set the View's visibility back on the main UI Thread
                        relativeLayout.setVisibility(View.INVISIBLE);
                        btnGetDirection.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();

        //bundle
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            name = getIntent().getExtras().getString("name");
            date = getIntent().getExtras().getString("date");
            address = getIntent().getExtras().getString("address");
        }

        bandName.setText(name);
        storageReference = FirebaseStorage.getInstance().getReference();

        valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ShowDetails showDetails1 = child.getValue(ShowDetails.class);

                    if(showDetails1.getDate().equals(date) && showDetails1.getBandName().equals(name) && showDetails1.getAddress().equalsIgnoreCase(address)) {
                        showDetails = showDetails1;

                        getPhotos(showDetails.getPlaceid());
                        //getting date obj
                        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");

                        String receivedDate = showDetails.getDate();
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                        Date DateshowDate = null;
                        Date DatetodayDate = null;

                        try {
                            DateshowDate = sdf.parse(receivedDate);
                        }
                        catch (Exception e){

                        }
                        String[] monthValue = {"Jan","Feb","Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

                        String monthval = showDetails.getDate();
                        String[] split = monthval.split("/");

                        int month = Integer.parseInt(split[1]);

                        String date = split[0].concat(" ").concat(monthValue[(month-1)]);

                        String dayOfTheWeek = (String) DateFormat.format("EEEE", DateshowDate);
                        String newDayOfTheWeek = dayOfTheWeek.substring(0,3);
                        String timeAndDate = dayOfTheWeek.concat(" " + date.concat("    |    "+showDetails.getStartTime() + "  to  " + showDetails.getEndTime()));

                        tvLocation.setText(showDetails.getLocationName());
                        tvAddress.setText(showDetails.getAddress());
                        tvTime.setText(showDetails.getStartTime().concat(" - " + showDetails.getEndTime()));
                        tvDate.setText(dayOfTheWeek.concat(" " + split[0] + " " + monthValue[(month-1)]));
                        if(!showDetails.getEntryFee().trim().equalsIgnoreCase("free") && !showDetails.getEntryFee().trim().equals("0")){
                            tvEntryFee.setText("$".concat(showDetails.getEntryFee()).concat(" Entry Fee"));
                        }
                        else{
                            tvEntryFee.setText("Free Entry");
                        }

                        if(showDetails.getGenre().contains(",")) {
                            tvGenre.setText(correctGenreString(showDetails.getGenre()));
                        }
                        else{
                            tvGenre.setText(showDetails.getGenre());
                        }
                        uid = showDetails.getUserid();

                        //pass the userid into this method loadImage(x)
                        loadImage(showDetails.getUserid());

                        final Query querydbref2 = databaseReference2.orderByKey().equalTo(showDetails.getUserid());
                        //for aboutband
                        querydbref2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot child : dataSnapshot.getChildren()) {

                                    UserDetails userDetails = child.getValue(UserDetails.class);
                                    tvAboutBand.setText(userDetails.getAbout());

                                    if(!userDetails.getinstagramName().equals("")){
                                        relativeLayoutSocialMediaInstagram.setVisibility(View.VISIBLE);
                                        instagramName = userDetails.getinstagramName();
                                        querydbref2.removeEventListener(this);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener1);
        btnGetDirection.setOnClickListener(this);
        btnShare.setOnClickListener(this);

    }




    public String correctGenreString(String genre){

        String genreString = genre;
        String match = ",";
        int positionofmatch = genreString.lastIndexOf(match);

        String fixedGenreString = genreString.substring(0,positionofmatch)+ " and" +genreString.substring(positionofmatch+1);
        return fixedGenreString;
    }


    public void loadImage(String uid){
        //reading of image
        final StorageReference pathref = storageReference.child("userid"+ uid +"/pic.jpg");

        pathref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                // Load the image using Glide
                Glide.with(getApplication().getApplicationContext())
                        .load(uri.toString())
                        .into(imageView);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Glide.with(getApplication().getApplicationContext())
                        .load(R.drawable.bandpicblackwhite)
                        .apply(new RequestOptions().override(3000, 2000))
                        .into(imageView);
            }
        });
    }

    @Override
    public void onClick(View view) {


        if(view.getId() == R.id.buttonGetDirections) {

            Toasty.normal(this, "Loading Google Map", Toast.LENGTH_SHORT).show();

            //open google map navigation mode
            Uri gmmIntentUri = Uri.parse("geo:" + showDetails.getLatLng() + "?z=1&q=" + showDetails.getLocationName());

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
        else if(view == relativeLayoutSocialMediaInstagram){
            //get instagram user id and prompt open facebook
            openInstagramAccount(instagramName);

        }
        else if(view.equals(relativeLayoutBandTab)){
            relativeLayoutShowTabBody.setVisibility(View.INVISIBLE);
            relativeLayoutDimmer.setAlpha(0.479f);

        }
        else if(view.equals(relativeLayoutShowTab)){
            relativeLayoutShowTabBody.setVisibility(View.VISIBLE);
            relativeLayoutDimmer.setAlpha(1.0f);
        }
        else if(view.equals(btnShare)){

            String bandname = capitalize(showDetails.getBandName());
            String[] split = showDetails.getAddress().split(",");
            String locationaddress = showDetails.getLocationName().concat(" (" + split[0] + ")");
            String time = showDetails.getStartTime().concat(" to ").concat(showDetails.getEndTime());
            String[] split2 = showDetails.getDate().split("/");
            String[] monthValue = {"January","February","March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            int month = Integer.parseInt(split2[1]);
            String dateformated = split2[0] + " " + monthValue[(month-1)];
            String dateandtime = dateformated.concat(", " + time);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "GIGFINDR'S GIG SHARING");
            String string = "*GIGFINDR'S GIG SHARING*\n\n" +
                    "Hey, thought you would be interested in this gig!\n\n" +
                    "Band: " + bandname +"\n" + "Location: " + locationaddress + "\n" + "Date & Time: " + dateandtime +
                    "\n\nDownload GigFindr for more gigs now at https://play.google.com/store/apps/details?id=" + this.getPackageName() ;
            intent.putExtra(Intent.EXTRA_TEXT, string);
            startActivity(Intent.createChooser(intent, "Share with"));
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
//                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                return true;
//            case R.id.showNotification:
//                //notification code
//                Calendar c = Calendar.getInstance();
//                String date = tvTimeAndDate.getText().toString();
//                String[] split = date.split("/");
//
//                Log.d("day", split[0]);
//                Log.d("month", split[1]);
//                Log.d("year", split[2]);
//
//                //year, month, day, hour of day, min, second FORMAT
//                c.set(Integer.parseInt(split[2]), Integer.parseInt(split[1]) - 1, Integer.parseInt(split[0]), 0, 0, 0);
//
//
//                Intent intent = new Intent(this, Receiver.class);
//                intent.putExtra("name", tvBandName.getText().toString());
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1253, intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
//
//                AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//                am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//                Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show();
//                Log.d("c.getTime: ", c.getTime() + "");
//                Log.d("c.getTimeInMilli: ", c.getTimeInMillis() + "");
//                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openInstagramAccount(String instagramName){

        Uri uri = Uri.parse("http://instagram.com/" + instagramName + "/");
        Intent insta = new Intent(Intent.ACTION_VIEW, uri);
        insta.setPackage("com.instagram.android");

        if (isIntentAvailable(ViewDetailedShowActivity.this, insta)){
            startActivity(insta);
        } else{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + instagramName)));
        }
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void getPhotos(String placeid) {
//        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeid);
//        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
//                // Get the list of photos.
//                PlacePhotoMetadataResponse photos = task.getResult();
//                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
//                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
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
//                            imageViewMap.setImageBitmap(bitmap);
//                            relativeLayoutDefaultPlaceCover.setVisibility(View.GONE);
//                        }
//                    });
//
//                }
//                else{
//                    int number = new Random().nextInt((3 - 1) + 1) + 1;
//
//                    int id = ViewDetailedShowActivity.this.getResources().getIdentifier("drawable/" + "defaultplace" + number, null, ViewDetailedShowActivity.this.getPackageName());
//                    imageViewMap.setImageResource(id);
//                    relativeLayoutDefaultPlaceCover.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);

        // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(placeid, fields).build();

        Places.initialize(getApplicationContext(), getString(R.string.google_api_key));
        final PlacesClient placesClient = Places.createClient(this);
        placesClient.fetchPlace(placeRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                Place place = fetchPlaceResponse.getPlace();
                if(place.getPhotoMetadatas() == null){
                    int number = new Random().nextInt((3 - 1) + 1) + 1;
//
                    int id = ViewDetailedShowActivity.this.getResources().getIdentifier("drawable/" + "defaultplace" + number, null, ViewDetailedShowActivity.this.getPackageName());
                    imageViewMap.setImageResource(id);
                    relativeLayoutDefaultPlaceCover.setVisibility(View.VISIBLE);
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

                            Glide.with(getApplication().getApplicationContext())
                                    .load(bitmap)
                                    .into(imageViewMap);

//                            imageViewMap.setImageBitmap(bitmap);
                            relativeLayoutDefaultPlaceCover.setVisibility(View.GONE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener1);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
